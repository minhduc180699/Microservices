package com.ds.dssearcher.repository;

import com.ds.dssearcher.entity.*;
import com.ds.dssearcher.model.DocContentModel;
import com.ds.dssearcher.model.FeedModel;
import com.ds.dssearcher.util.Constant;
import com.google.common.collect.Sets;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
public class SearchRepository {
    public static final Logger logger = LoggerFactory.getLogger(SearchRepository.class);
    @Qualifier(value = "client")
    @Autowired
    private RestHighLevelClient esClient;

    private static final List<String> listObject = new ArrayList<>();
    private static final int threadNumber = 10;


    public SearchRepository() {
        listObject.add(Constant.DocumentFieldName.TITLE_FIELD);
        listObject.add(Constant.DocumentFieldName.CONTENT_FIELD);
        listObject.add(Constant.DocumentFieldName.MESSAGE_FIELD);
        listObject.add(Constant.DocumentFieldName.AUTHOR_FIELD);
        listObject.add(Constant.DocumentFieldName.LANGUAGE_FIELD);
        listObject.add(Constant.DocumentFieldName.SERVICE_TYPE_FIELD);
        listObject.add(Constant.DocumentFieldName.SOURCE_FIELD);
        listObject.add(Constant.DocumentFieldName.IMAGES_FIELD);
        listObject.add(Constant.DocumentFieldName.PUBLISHED_FIELD);
        listObject.add(Constant.DocumentFieldName.VIDEO_FIELD);
        listObject.add(Constant.DocumentFieldName.FAVICON_URL_FIELD);
        listObject.add(Constant.DocumentFieldName.FAVICON_BASE64_FIELD);
        listObject.add(Constant.DocumentFieldName.OG_IMAGE_URL_FIELD);
        listObject.add(Constant.DocumentFieldName.OG_IMAGE_BASE64_FIELD);
        listObject.add(Constant.DocumentFieldName.IMAGE_URL_FIELD);
        listObject.add(Constant.DocumentFieldName.IMAGE_BASE64_FIELD);
        listObject.add(Constant.DocumentFieldName.SERVICE_TYPE_FIELD);
        listObject.add(Constant.DocumentFieldName.WRITER_ID_FIELD);
        listObject.add(Constant.DocumentFieldName.ENTITY_LINKING_FIELD);
    }

    public DetailDocumentEntity getById(String id) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        QueryBuilder keywordQuery = buildKeywordQuery(id, Constant.DocumentFieldName.ID_FIELD);
        if (keywordQuery != null)
            boolQuery.must(keywordQuery);

        SearchRequest searchRequest = new SearchRequest("ds-global*");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.storedFields(listObject);
        searchSourceBuilder.query(boolQuery);
        searchSourceBuilder.size(10);
        searchSourceBuilder.trackTotalHits(true);
        searchRequest.source(searchSourceBuilder);
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());

        SearchResponse searchResponse = null;
        try {
            searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHits hits = searchResponse.getHits();
        Iterator<SearchHit> iter = hits.iterator();
        DetailDocumentEntity doc = null;
        while (iter.hasNext()) {
            SearchHit hit = iter.next();
            try {
                doc = new DetailDocumentEntity();
                doc = doc.covertObjectToDoc(hit);
            } catch (Exception e) {
                logger.error("Error content: " + e.getMessage());
            }

        }
        return doc;
    }

    public SearchResult searchKeyword(String keyword, int page, String searchType, String language, String fromDate, String toDate, Integer size, List<String> authors, String channels, String sortBy, String reverseSort,
                                      Integer score, String from, String util) {
        SearchResult result = new SearchResult();
        try {
            List<DocumentEntity> docList = new ArrayList<>();
            List<String> indexers = new ArrayList<>();
            indexers.add("ds-global-news*");
            indexers.add("ds-global-video*");
            indexers.add("ds-global-pdf*");
            indexers.add("ds-global-ppt*");
            indexers.add("ds-global-doc*");
            indexers.add("ds-global-dart*");
            indexers.add("ds-global-investing*");
            indexers.add("ds-global-twitter*");
            indexers.add("ds-global-linkedin*");
            indexers.add("ds-global-facebook*");
            indexers.add("ds-global-blog*");
            indexers.add("ds-global-youtube*");
            indexers.add("ds-global-instagram*");
            indexers.add("ds-global-web*");

            if (channels != null && !channels.isEmpty()) {

                String[] arrUrls = channels.split(",");
                ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);
                List<Callable<Integer>> tasks = new LinkedList<>();
                for (String value : arrUrls) {
                    Callable<Integer> callableTask = () -> {
                        logger.info("Thread Id = {}", Thread.currentThread().getId());
                        Map<String, String> stringMap = convertUrlChannelsToList(value);
                        String domain = null;
                        String subDomain = null;
                        String category = null;
                        String subCategory = null;
                        if (stringMap != null) {
                            domain = stringMap.get("domain");
                            subDomain = stringMap.get("subDomain");
                            category = stringMap.get("category");
                            subCategory = stringMap.get("subCategory");
                        }
                        for (String indexName : indexers) {
                            List<DocumentEntity> documentEntityList = searchKeywordFromIndex(indexName, keyword, page, searchType, language, fromDate, toDate, size, authors, domain, subDomain, category, subCategory, sortBy, reverseSort,
                                    score, from, util);
                            if (documentEntityList != null && documentEntityList.size() > 0) {
                                docList.addAll(documentEntityList);
                            }
                        }
                        return 1;
                    };
                    tasks.add(callableTask);
                }
                executorService.invokeAll(tasks);
                if (!executorService.isShutdown()) {
                    executorService.shutdown();
                }
                logger.info("Thread shutdown");
            } else {
                for (String indexName : indexers) {
                    List<DocumentEntity> documentEntityList = searchKeywordFromIndex(indexName, keyword, page, searchType, language, fromDate, toDate, size, authors, null, null, null, null, sortBy, reverseSort,
                            score, from, util);
                    if (documentEntityList != null && documentEntityList.size() > 0) {
                        docList.addAll(documentEntityList);
                    }
                }
            }
            result.setData(docList);
            result.setResult("success");
            result.setResult_code(0);
        } catch (Exception e) {
            e.printStackTrace();
            result.setData(new ArrayList<>());
            result.setResult("faild");
            result.setResult_code(999);
        }
        return result;
    }

    public List<DocumentEntity> searchKeywordFromIndex(String indexName, String keyword, int page, String searchType, String language, String fromDate, String toDate, Integer size, List<String> authors,
                                                       String domainUrl, String subdomainUrl, String categoryUrl, String subcategoryUrl, String sortBy, String reverseSort, Integer score, String from,
                                                       String util) {

        if (indexName == null) return null;

        String type = searchType(indexName);

        if (type == null) return null;

        if (searchType == null) searchType = type;
        else {
            if (searchType.equalsIgnoreCase("Social")) {
                if (!type.equalsIgnoreCase("Dart") && !type.equalsIgnoreCase("Investing") && !type.equalsIgnoreCase("Twitter")
                        && !type.equalsIgnoreCase("Linkedin") && !type.equalsIgnoreCase("Facebook")) return null;
            } else {
                if (!searchType.equalsIgnoreCase(type)) return null;
            }

        }

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        QueryBuilder keywordQuery = buildContainQuery(keyword, Constant.DocumentFieldName.SEARCH_CONTENT_FIELD);
        QueryBuilder languageQuery = buildSingleKeywordQuery(language, Constant.DocumentFieldName.LANGUAGE_FIELD);
        QueryBuilder dateQuery = buildDateFilterQuery(fromDate, toDate);
        QueryBuilder dateSearchQuery = buildDateFilterQuery(from, util);
        QueryBuilder authorQuery = buildContainOneOfQuery(authors, Constant.DocumentFieldName.WRITER_SEARCH_FIELD);
        QueryBuilder domainUrlQuery = null;
        QueryBuilder subDomainUrlQuery = null;
        //  QueryBuilder categoryUrlQuery = null;
        QueryBuilder subCategoryUrlQuery = null;
        if (domainUrl != null) {
            domainUrlQuery = buildQuery(domainUrl.trim().toLowerCase(), Constant.DocumentFieldName.DOMAIN_URL_FIELD);
        }
        if (subdomainUrl != null && categoryUrl != null) {
            subDomainUrlQuery = buildContainOneOfQuery(subdomainUrl.trim().toLowerCase(), categoryUrl.trim().toLowerCase(),
                    Constant.DocumentFieldName.SUB_DOMAIN_URL_FIELD, Constant.DocumentFieldName.CATEGORY_FIELD);
        }

        if (subcategoryUrl != null) {
            subCategoryUrlQuery = buildQuery(subcategoryUrl.trim().toLowerCase(), Constant.DocumentFieldName.SUB_CATEGORY_FIELD);
        }
        if (keywordQuery != null)
            boolQuery.must(keywordQuery);

        if (languageQuery != null)
            boolQuery.must(languageQuery);

        if (dateQuery != null)
            boolQuery.must(dateQuery);

        if (dateSearchQuery != null)
            boolQuery.must(dateSearchQuery);

        if (authorQuery != null) {
            boolQuery.must(authorQuery);
        }

        if (domainUrlQuery != null) {
            boolQuery.must(domainUrlQuery);
        }

        if (subDomainUrlQuery != null) {
            boolQuery.must(subDomainUrlQuery);
        }

//        if(categoryUrlQuery != null){
//            boolQuery.must(categoryUrlQuery);
//        }

        if (subCategoryUrlQuery != null) {
            boolQuery.must(subCategoryUrlQuery);
        }

        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQuery);
        int fromPage = (page - 1) * size;
        searchSourceBuilder.from(fromPage).size(size);

        String[] sorts = sortBy.split(",");
        for (String sort : sorts) {

            if (sort.equalsIgnoreCase("date")) {
                if (reverseSort.equalsIgnoreCase("false")) {
                    searchSourceBuilder.sort(Constant.DocumentFieldName.PUBLISHED_FIELD, SortOrder.DESC);
                } else if (reverseSort.equalsIgnoreCase("true")) {
                    searchSourceBuilder.sort(Constant.DocumentFieldName.PUBLISHED_FIELD, SortOrder.ASC);
                }
            } else if (sort.equalsIgnoreCase("score")) {
                if (reverseSort.equalsIgnoreCase("false")) {
                    searchSourceBuilder.sort("_score", SortOrder.DESC);
                } else if (reverseSort.equalsIgnoreCase("true")) {
                    searchSourceBuilder.sort("_score", SortOrder.ASC);
                }
            }

        }

        searchSourceBuilder.storedFields(listObject);
        searchSourceBuilder.query(boolQuery);
        searchSourceBuilder.trackTotalHits(true);
        searchSourceBuilder.trackScores(true);
        searchRequest.source(searchSourceBuilder);
        if (score != null) {
            searchRequest.source().minScore(score);
        }

        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());


        SearchResponse searchResponse = null;
        try {
            searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHits hits = searchResponse.getHits();
        Iterator<SearchHit> iter = hits.iterator();
        List<DocumentEntity> documentEntityList = new ArrayList<>();
        while (iter.hasNext()) {
            DocumentEntity doc = new DocumentEntity();
            SearchHit hit = iter.next();
            try {
                DocumentEntity documentEntity = doc.covertObjectToDoc(hit, searchType, keyword, type);
                if (documentEntity != null) {
                    documentEntityList.add(documentEntity);
                }
            } catch (Exception e) {
                logger.error("Error content: " + e.getMessage());
            }

        }
        return documentEntityList;
    }

    public SearchResultPeople searchPeople(String channels, List<String> authors, List<String> authorIds, int page, int size, String language) {
        SearchResultPeople result = new SearchResultPeople();
        try {
            List<DocumentPeopleEntity> docList = new ArrayList<>();
            List<String> indexers = new ArrayList<>();
            indexers.add("ds-global-twitter*");
            indexers.add("ds-global-facebook*");
            indexers.add("ds-global-instagram*");

            if (channels != null && !channels.isEmpty()) {
                String[] arrUrls = channels.split(",");
                ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);
                List<Callable<Integer>> tasks = new LinkedList<>();
                for (String value : arrUrls) {
                    Callable<Integer> callableTask = () -> {
                        logger.info("Thread Id = {}", Thread.currentThread().getId());
                        Map<String, String> stringMap = convertUrlChannelsToList(value);
                        String domain = null;
                        String subDomain = null;
                        String category = null;
                        String subCategory = null;
                        if (stringMap != null) {
                            domain = stringMap.get("domain");
                            subDomain = stringMap.get("subDomain");
                            category = stringMap.get("category");
                            subCategory = stringMap.get("subCategory");
                        }
                        for (String indexName : indexers) {
                            List<DocumentPeopleEntity> documentEntityList = searchPeopleFromIndex(indexName, authors, authorIds, page, size, language, domain, subDomain, category, subCategory);
                            if (documentEntityList != null && documentEntityList.size() > 0) {
                                docList.addAll(documentEntityList);
                            }
                        }
                        return 1;
                    };
                    tasks.add(callableTask);
                }
                executorService.invokeAll(tasks);
                logger.info("shutdown thread search people");
                if (!executorService.isShutdown()) {
                    executorService.shutdown();
                }
            } else {
                for (String indexName : indexers) {
                    List<DocumentPeopleEntity> documentEntityList = searchPeopleFromIndex(indexName, authors, authorIds, page, size, language, null, null, null, null);
                    if (documentEntityList != null && documentEntityList.size() > 0) {
                        docList.addAll(documentEntityList);
                    }
                }
            }
            result.setData(docList);
            result.setResult("success");
            result.setResult_code(0);
        } catch (Exception e) {
            e.printStackTrace();
            result.setData(new ArrayList<>());
            result.setResult("faild");
            result.setResult_code(999);
        }
        return result;
    }

    public List<DocumentPeopleEntity> searchPeopleFromIndex(String indexName, List<String> authors, List<String> authorIds, int page, int size, String language,
                                                            String domainUrl, String subdomainUrl, String categoryUrl, String subcategoryUrl) {

        if (indexName == null) return null;
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        QueryBuilder languageQuery = buildSingleKeywordQuery(language, Constant.DocumentFieldName.LANGUAGE_FIELD);
        QueryBuilder authorQuery = buildContainOneOfQuery(authors, Constant.DocumentFieldName.WRITER_SEARCH_FIELD);


        QueryBuilder domainUrlQuery = null;
        QueryBuilder subDomainUrlQuery = null;
        //  QueryBuilder categoryUrlQuery = null;
        QueryBuilder subCategoryUrlQuery = null;
        if (domainUrl != null) {
            domainUrlQuery = buildQuery(domainUrl.trim().toLowerCase(), Constant.DocumentFieldName.DOMAIN_URL_FIELD);
        }
        if (subdomainUrl != null && categoryUrl != null) {
            subDomainUrlQuery = buildContainOneOfQuery(subdomainUrl.trim().toLowerCase(), categoryUrl.trim().toLowerCase(),
                    Constant.DocumentFieldName.SUB_DOMAIN_URL_FIELD, Constant.DocumentFieldName.CATEGORY_FIELD);
        }

        if (subcategoryUrl != null) {
            subCategoryUrlQuery = buildQuery(subcategoryUrl.trim().toLowerCase(), Constant.DocumentFieldName.SUB_CATEGORY_FIELD);
        }

        if (languageQuery != null)
            boolQuery.must(languageQuery);

        if (authorIds != null && !authorIds.isEmpty())
            boolQuery.must(QueryBuilders.termsQuery(Constant.DocumentFieldName.WRITER_ID_FIELD, authorIds));

        if (authorQuery != null) {
            boolQuery.must(authorQuery);
        }

        if (domainUrlQuery != null) {
            boolQuery.must(domainUrlQuery);
        }

        if (subDomainUrlQuery != null) {
            boolQuery.must(subDomainUrlQuery);
        }

        if (subCategoryUrlQuery != null) {
            boolQuery.must(subCategoryUrlQuery);
        }

        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQuery);
        int from = (page - 1) * size;
        searchSourceBuilder.from(from).size(size);

        searchSourceBuilder.sort(Constant.DocumentFieldName.PUBLISHED_FIELD, SortOrder.DESC);

        searchSourceBuilder.storedFields(listObject);
        searchSourceBuilder.query(boolQuery);
        searchSourceBuilder.trackTotalHits(true);
        searchRequest.source(searchSourceBuilder);
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());

        SearchResponse searchResponse = null;
        try {
            searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHits hits = searchResponse.getHits();
        Iterator<SearchHit> iter = hits.iterator();
        List<DocumentPeopleEntity> documentEntityList = new ArrayList<>();
        while (iter.hasNext()) {
            DocumentPeopleEntity doc = new DocumentPeopleEntity();
            SearchHit hit = iter.next();
            try {
                DocumentPeopleEntity documentEntity = doc.convertObjectToDoc(hit);
                if (documentEntity != null) {
                    documentEntityList.add(documentEntity);
                }
            } catch (Exception e) {
                logger.error("Error content: " + e.getMessage());
            }

        }
        return documentEntityList;
    }

    public SearchResultDataSource searchDataOfDatasources(String dataSourceIds, String channels, int page, int size, String seviceType, String lang) {
        SearchResultDataSource result = new SearchResultDataSource();
        List<String> indexers = new ArrayList<>();
        try {
            List<DocumentSourceDataEntity> docList = new ArrayList<>();
            if (seviceType != null && !seviceType.isEmpty()) {
                List<String> serviceTypes = Arrays.asList(seviceType.split(","));
                for (String ser : serviceTypes) {
                    if (ser.toLowerCase().contains("facebook")) {
                        indexers.add("ds-global-facebook");
                    } else if (ser.toLowerCase().contains("twitter")) {
                        indexers.add("ds-global-twitter");
                    } else if (ser.toLowerCase().contains("linkedin")) {
                        indexers.add("ds-global-linkedin");
                    } else if (ser.toLowerCase().contains("instagram")) {
                        indexers.add("ds-global-instagram");
                    } else if (ser.toLowerCase().contains("investing")) {
                        indexers.add("ds-global-investing");
                    } else if (ser.toLowerCase().contains("dart")) {
                        indexers.add("ds-global-dart");
                    } else if (ser.toLowerCase().contains("webdata")) {
                        indexers.add("ds-global-webdata");
                    }
                }

            } else {
                indexers.add("ds-global");
            }

            int index = 0;
            for (String indexer : indexers) {
                if (lang != null && !lang.isEmpty() && LanguageEnum.checkLang(lang)) {
                    indexers.set(index, indexer + "-" + LanguageEnum.getLanguage(lang).toLowerCase() + "*");
                } else {
                    indexers.set(index, indexer + "*");
                }
                index++;
            }


            if (channels != null && !channels.isEmpty()) {
//                    String[] arrUrls = channels.split(",");
//                    ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);
//                    List<Callable<Integer>> tasks = new LinkedList<>();
//                    for (String value : arrUrls) {
//
//                        Callable<Integer> callableTask = () -> {
//                            Map<String, String> stringMap = convertUrlChannelsToList(value);
//                            String domain = null;
//                            String subDomain = null;
//                            String category = null;
//                            String subCategory = null;
//                            if (stringMap != null) {
//                                domain = stringMap.get("domain");
//                                subDomain = stringMap.get("subDomain");
//                                category = stringMap.get("category");
//                                subCategory = stringMap.get("subCategory");
//                            }
//
//                            List<DocumentSourceDataEntity> documentEntityList = searchDataOfDatasourcesFromEs(indexers, Arrays.asList(dataSourceIds.split(",")), page, size, domain, subDomain, category, subCategory);
//                            if (documentEntityList != null && documentEntityList.size() > 0) {
//                                docList.addAll(documentEntityList);
//                            }
//
//                            return 1;
//                        };
//                        tasks.add(callableTask);
//                    }
//                    executorService.invokeAll(tasks);
//                    if(!executorService.isShutdown()) {
//                        executorService.shutdown();
//                    }

            } else {
                result = searchDataOfDatasourcesFromEs(indexers, Arrays.asList(dataSourceIds.split(",")), page, size, null, null, null, null);
            }

//            result.setData(docList);
            result.setResult("success");
            result.setResult_code(0);
        } catch (Exception e) {
            e.printStackTrace();
            result.setData(new ArrayList<>());
            result.setResult("faild");
            result.setResult_code(999);
        }
        return result;
    }

    public SearchResultDataSource searchDataOfDatasourcesFromEs(List<String> indexers, List<String> dataSourceIds, int page, int size, String domainUrl, String subdomainUrl,
                                                                String categoryUrl, String subcategoryUrl) {


        if (indexers == null || indexers.isEmpty()) return null;
        SearchResultDataSource searchResultDataSource = new SearchResultDataSource();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        QueryBuilder domainUrlQuery = null;
        QueryBuilder subDomainUrlQuery = null;
        QueryBuilder subCategoryUrlQuery = null;

        if (dataSourceIds != null && !dataSourceIds.isEmpty()) {
            boolQuery.must(QueryBuilders.termsQuery(Constant.DocumentFieldName.WEB_SOURCE_ID, dataSourceIds));
        }

        if (domainUrl != null) {
            domainUrlQuery = buildQuery(domainUrl.trim().toLowerCase(), Constant.DocumentFieldName.DOMAIN_URL_FIELD);
        }
        if (subdomainUrl != null && categoryUrl != null) {
            subDomainUrlQuery = buildContainOneOfQuery(subdomainUrl.trim().toLowerCase(), categoryUrl.trim().toLowerCase(),
                    Constant.DocumentFieldName.SUB_DOMAIN_URL_FIELD, Constant.DocumentFieldName.CATEGORY_FIELD);
        }

        if (subcategoryUrl != null) {
            subCategoryUrlQuery = buildQuery(subcategoryUrl.trim().toLowerCase(), Constant.DocumentFieldName.SUB_CATEGORY_FIELD);
        }

        if (domainUrlQuery != null) {
            boolQuery.must(domainUrlQuery);
        }

        if (subDomainUrlQuery != null) {
            boolQuery.must(subDomainUrlQuery);
        }

        if (subCategoryUrlQuery != null) {
            boolQuery.must(subCategoryUrlQuery);
        }

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexers.toArray(new String[indexers.size()]));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQuery);
        int from = (page - 1) * size;
        searchSourceBuilder.query(boolQuery);
        searchSourceBuilder.trackTotalHits(true);
        searchSourceBuilder.from(from).size(size);
        searchSourceBuilder.storedFields(listObject);
        searchSourceBuilder.sort(Constant.DocumentFieldName.PUBLISHED_FIELD, SortOrder.DESC);
        searchRequest.source(searchSourceBuilder);
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());

        SearchResponse searchResponse = null;
        try {
            searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<DocumentSourceDataEntity> documentEntityList = new ArrayList<>();
        SearchHits hits = searchResponse.getHits();
        Iterator<SearchHit> iter = hits.iterator();
        while (iter.hasNext()) {
            DocumentSourceDataEntity doc = new DocumentSourceDataEntity();
            SearchHit hit = iter.next();
            try {
                DocumentSourceDataEntity documentEntity = doc.covertObjectToDoc(hit);
                if (documentEntity != null) {
                    documentEntityList.add(documentEntity);
                }
            } catch (Exception e) {
                logger.error("Error content: " + e.getMessage());
            }

        }

        searchResultDataSource.setData(documentEntityList);
        searchResultDataSource.setTotalAll(hits.getTotalHits().value);
        searchResultDataSource.setTotal(documentEntityList.size());
        return searchResultDataSource;
    }


    public List<DocumentDataSourceEntity> searchDataOfDatasourcesFromIndex(List<String> indexers, List<String> dataSourceIds, String domainUrl, String subdomainUrl,
                                                                           String categoryUrl, String subcategoryUrl) {


        if (indexers == null || indexers.isEmpty()) return null;
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        QueryBuilder domainUrlQuery = null;
        QueryBuilder subDomainUrlQuery = null;
        QueryBuilder subCategoryUrlQuery = null;

        if (dataSourceIds != null && !dataSourceIds.isEmpty()) {
            boolQuery.must(QueryBuilders.termsQuery(Constant.DocumentFieldName.WEB_SOURCE_ID, dataSourceIds));
        }

        if (domainUrl != null) {
            domainUrlQuery = buildQuery(domainUrl.trim().toLowerCase(), Constant.DocumentFieldName.DOMAIN_URL_FIELD);
        }
        if (subdomainUrl != null && categoryUrl != null) {
            subDomainUrlQuery = buildContainOneOfQuery(subdomainUrl.trim().toLowerCase(), categoryUrl.trim().toLowerCase(),
                    Constant.DocumentFieldName.SUB_DOMAIN_URL_FIELD, Constant.DocumentFieldName.CATEGORY_FIELD);
        }

        if (subcategoryUrl != null) {
            subCategoryUrlQuery = buildQuery(subcategoryUrl.trim().toLowerCase(), Constant.DocumentFieldName.SUB_CATEGORY_FIELD);
        }

        if (domainUrlQuery != null) {
            boolQuery.must(domainUrlQuery);
        }

        if (subDomainUrlQuery != null) {
            boolQuery.must(subDomainUrlQuery);
        }

        if (subCategoryUrlQuery != null) {
            boolQuery.must(subCategoryUrlQuery);
        }

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexers.toArray(new String[indexers.size()]));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQuery);
//        int from = (page-1) * size;
        searchSourceBuilder.query(boolQuery);
        searchSourceBuilder.trackTotalHits(true);
        searchSourceBuilder.aggregation(AggregationBuilders.terms("agg_name").field(Constant.DocumentFieldName.WEB_SOURCE_ID).size(5000));
//                .subAggregation(
//                AggregationBuilders.topHits("documents").explain(true)));
//        .from(from).size(size).storedFields(listObject).sort(Constant.DocumentFieldName.PUBLISHED_FIELD, SortOrder.DESC))

        searchRequest.source(searchSourceBuilder);
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());

        SearchResponse searchResponse = null;
        try {
            searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<DocumentDataSourceEntity> documentEntityList = new ArrayList<>();
//        if(searchResponse.getAggregations() != null) {
        Terms agg_name_aggregation = searchResponse.getAggregations().get("agg_name");

        for (Terms.Bucket bucket : agg_name_aggregation.getBuckets()) {
//                TopHits topHits = bucket.getAggregations().get("documents");
            DocumentDataSourceEntity documentDataSourceEntity = new DocumentDataSourceEntity();
            documentDataSourceEntity.setDatasource_id(bucket.getKeyAsString());
            documentDataSourceEntity.setTotalAll(bucket.getDocCount());
//                SearchHits hits = topHits.getHits();
//                Iterator<SearchHit> iter = hits.iterator();
//                List<DocumentDataSourceEntity.DataSourceEntity> dataSourceEntities = new ArrayList<>();
//                while (iter.hasNext()) {
//                    DocumentDataSourceEntity.DataSourceEntity doc = new DocumentDataSourceEntity.DataSourceEntity();
//                    SearchHit hit = iter.next();
//                    try {
//                        DocumentDataSourceEntity.DataSourceEntity documentEntity = doc.covertObjectToDoc(hit);
//                        if (documentEntity != null) {
//                            dataSourceEntities.add(documentEntity);
//                        }
//                    } catch (Exception e) {
//                        logger.error("Error content: " + e.getMessage());
//                    }
//
//                }
//                documentDataSourceEntity.setData(dataSourceEntities);
            documentEntityList.add(documentDataSourceEntity);
        }
//        }
        return documentEntityList;
    }

    public ResultCountDatasource countDataSourceById(String dataSourceIds, String channels, String seviceType, String lang) {
        ResultCountDatasource result = new ResultCountDatasource();
        List<String> indexers = new ArrayList<>();
        List<ResultCountDatasource.CountDocument> docList = new ArrayList<>();
        try {
            if (seviceType != null && !seviceType.isEmpty()) {
                List<String> serviceTypes = Arrays.asList(seviceType.split(","));
                for (String ser : serviceTypes) {
                    if (ser.toLowerCase().contains("facebook")) {
                        indexers.add("ds-global-facebook");
                    } else if (ser.toLowerCase().contains("twitter")) {
                        indexers.add("ds-global-twitter");
                    } else if (ser.toLowerCase().contains("linkedin")) {
                        indexers.add("ds-global-linkedin");
                    } else if (ser.toLowerCase().contains("instagram")) {
                        indexers.add("ds-global-instagram");
                    } else if (ser.toLowerCase().contains("investing")) {
                        indexers.add("ds-global-investing");
                    } else if (ser.toLowerCase().contains("dart")) {
                        indexers.add("ds-global-dart");
                    } else if (ser.toLowerCase().contains("webdata")) {
                        indexers.add("ds-global-webdata");
                    }
                }

            } else {
                indexers.add("ds-global");
            }

            int index = 0;
            for (String indexer : indexers) {
                if (lang != null && !lang.isEmpty() && LanguageEnum.checkLang(lang)) {
                    indexers.set(index, indexer + "-" + LanguageEnum.getLanguage(lang).toLowerCase() + "*");
                } else {
                    indexers.set(index, indexer + "*");
                }
                index++;
            }

            if (channels != null && !channels.isEmpty()) {
                String[] arrUrls = channels.split(",");
                ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);
                List<Callable<Integer>> tasks = new LinkedList<>();

                for (String value : arrUrls) {

                    Callable<Integer> callableTask = () -> {
                        Map<String, String> stringMap = convertUrlChannelsToList(value);
                        String domain = null;
                        String subDomain = null;
                        String category = null;
                        String subCategory = null;
                        if (stringMap != null) {
                            domain = stringMap.get("domain");
                            subDomain = stringMap.get("subDomain");
                            category = stringMap.get("category");
                            subCategory = stringMap.get("subCategory");
                        }

                        List<DocumentDataSourceEntity> documentEntityList = searchDataOfDatasourcesFromIndex(indexers, Arrays.asList(dataSourceIds.split(",")), domain, subDomain, category, subCategory);
                        if (documentEntityList != null && documentEntityList.size() > 0) {
                            List<ResultCountDatasource.CountDocument> countDocumentList = documentEntityList.stream()
                                    .map(documentDataSourceEntity -> new ResultCountDatasource.CountDocument(Integer.parseInt(documentDataSourceEntity.getDatasource_id()), documentDataSourceEntity.getTotalAll()))
                                    .collect(Collectors.toList());
                            docList.addAll(countDocumentList);
                        }

                        return 1;
                    };
                    tasks.add(callableTask);
                }
                executorService.invokeAll(tasks);
                if (!executorService.isShutdown()) {
                    executorService.shutdown();
                }

                Map<Integer, Long> map = docList.stream().collect(
                        Collectors.groupingBy(ResultCountDatasource.CountDocument::getData_source_id, Collectors.summingLong(ResultCountDatasource.CountDocument::getCount)));
                docList.clear();
                map.forEach((key, value) -> {
                    docList.add(new ResultCountDatasource.CountDocument(key, value));
                });

            } else {
                List<DocumentDataSourceEntity> documentEntityList = searchDataOfDatasourcesFromIndex(indexers, Arrays.asList(dataSourceIds.split(",")), null, null, null, null);
                if (documentEntityList != null && documentEntityList.size() > 0) {
                    List<ResultCountDatasource.CountDocument> countDocumentList = documentEntityList.stream()
                            .map(documentDataSourceEntity -> new ResultCountDatasource.CountDocument(Integer.parseInt(documentDataSourceEntity.getDatasource_id()), documentDataSourceEntity.getTotalAll()))
                            .collect(Collectors.toList());
                    docList.addAll(countDocumentList);
                }
            }

            result.setData(docList);
            result.setResult("success");
            result.setResult_code(0);
        } catch (Exception e) {
            e.printStackTrace();
            result.setData(new ArrayList<>());
            result.setResult("faild");
            result.setResult_code(999);
        }
        return result;
    }

    private Map<String, String> convertUrlChannelsToList(String channels) {
        if (channels == null || channels.isEmpty()) return null;
        Map<String, String> map = new HashMap<>();
        String[] arr = channels.split("/");
        if (arr.length > 2) {
            map.put("domain", arr[0]);
            map.put("subDomain", arr[1]);
            map.put("category", arr[1]);
            map.put("subCategory", arr[2]);
        } else {
            for (int i = 0; i < arr.length; i++) {
                if (i == 0) map.put("domain", arr[i]);
                if (i == 1) {
                    map.put("subDomain", arr[i]);
                    map.put("category", arr[i]);
                }
            }
        }
        return map;
    }

    private String searchType(String indexName) {

        if (indexName.contains("news")) {
            return "News";
        } else if (indexName.contains("video")) {
            return "Video";
        } else if (indexName.contains("doc")) {
            return "File:<doc>";
        } else if (indexName.contains("pdf")) {
            return "File:<pdf>";
        } else if (indexName.contains("ppt")) {
            return "File:<ppt>";
        } else if (indexName.contains("dart")) {
            return "Dart";
        } else if (indexName.contains("investing")) {
            return "Investing";
        } else if (indexName.contains("twitter")) {
            return "Twitter";
        } else if (indexName.contains("linkedin")) {
            return "Linkedin";
        } else if (indexName.contains("facebook")) {
            return "Facebook";
        } else if (indexName.contains("youtube")) {
            return "Youtube";
        } else if (indexName.contains("blog")) {
            return "Blog";
        } else if (indexName.contains("instagram")) {
            return "Instagram";
        }

        return null;
    }

    private QueryBuilder buildContainOneOfQuery(String keywords, String field) {
        if (keywords == null || keywords.isEmpty())
            return null;

        String[] words = keywords.split(" ");
        BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();
        searchQuery.minimumShouldMatch(1);// Must match at least one phrase_match
        for (String w : words) {
            QueryBuilder keywordQuery = QueryBuilders.matchPhraseQuery(field, w.trim());
            searchQuery.should(keywordQuery);
        }


        return searchQuery;
    }

    private QueryBuilder buildContainQuery(String keywords, String field) {
        if (keywords == null || keywords.isEmpty())
            return null;
        BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();
        if (keywords.contains("*")) {
            QueryBuilder keywordQuery = QueryBuilders.queryStringQuery(keywords.toLowerCase().trim()).field(field);
            searchQuery.must(keywordQuery);
            return searchQuery;
        }

        List<String> words1 = new ArrayList<>();
        List<String> words2 = new ArrayList<>();
        String strKey = keywords.toLowerCase().replaceAll("\".*?\"|-.*?\\s", "").trim();
        if (strKey.contains("-")) {
            String str = strKey.split(" ")[strKey.split(" ").length - 1];
            words2.add(strKey.split(" ")[strKey.split(" ").length - 1]);
            strKey = strKey.replaceAll(str, "").trim();

        }
        String[] words = !strKey.isEmpty() ? strKey.split(" ") : null;
        if (words != null) words1 = Arrays.asList(words);

        List<String> patterns = new ArrayList<>();
        if (keywords.contains("\"")) {
            patterns.add("\".*?\"");
        }
        if (keywords.contains("-")) {
            patterns.add("-.*?\\s");
        }

        for (String ptt : patterns) {
            Pattern pattern = Pattern.compile(ptt);
            Matcher matcher = pattern.matcher(keywords.toLowerCase());
            while (matcher.find()) {
                words2.add(matcher.group(0));
            }
        }
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Boolean checkMustNot = false;
        searchQuery.minimumShouldMatch(1);
        BoolQueryBuilder queryBuilder1 = QueryBuilders.boolQuery();
        BoolQueryBuilder queryBuilder2 = QueryBuilders.boolQuery();

        for (String w2 : words2) {
            QueryBuilder keywordQuery = QueryBuilders.matchPhraseQuery(field, w2.replaceAll("\"|-", "").trim());
            if (w2.contains("-")) {
                checkMustNot = true;
                boolQueryBuilder.mustNot(keywordQuery);
            } else {
                queryBuilder2.must(keywordQuery);
                if (!words1.isEmpty()) queryBuilder1.must(keywordQuery);
            }

        }

        if (queryBuilder1.hasClauses()) {
            for (String w1 : words1) {
                if (!w1.isEmpty()) {
                    queryBuilder1.must(QueryBuilders.matchPhraseQuery(field, w1.trim()));
                }
            }
        }
        if (queryBuilder1.hasClauses()) searchQuery.should(queryBuilder1);

        if (words1.size() > 1) {
            for (String w1 : words1) {
                BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
                for (String w2 : words2) {
                    if (w2.contains("\"")) {
                        QueryBuilder keywordQuery = QueryBuilders.matchPhraseQuery(field, w2.replaceAll("\"", "").trim());
                        queryBuilder.must(keywordQuery);
                    }

                }
                if (!w1.isEmpty()) {
                    queryBuilder.must(QueryBuilders.matchPhraseQuery(field, w1.trim()));
                }
                searchQuery.should(queryBuilder);

            }

        } else {
            if (words2.isEmpty() || (keywords.contains("-") && !keywords.contains("\"") && words1.size() == 1)) {
                BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
                queryBuilder.must(QueryBuilders.matchPhraseQuery(field, words1.get(0).trim()));
                searchQuery.should(queryBuilder);
            }


        }


        if (queryBuilder2.hasClauses()) searchQuery.should(queryBuilder2);
        return checkMustNot ? boolQueryBuilder.must(searchQuery) : searchQuery;
    }

    private QueryBuilder buildContainOneOfQuery(List<String> keywords, String field) {
        if (keywords == null || keywords.isEmpty())
            return null;
        BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();
        searchQuery.minimumShouldMatch(1);// Must match at least one phrase_match
        for (String w : keywords) {
            QueryBuilder keywordQuery = QueryBuilders.matchPhraseQuery(field, w.trim());
            searchQuery.should(keywordQuery);
        }


        return searchQuery;
    }

    private QueryBuilder buildContainOneOfQuery(String keyword1, String keyword2, String field1, String field2) {
        BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();
        searchQuery.minimumShouldMatch(1);// Must match at least one phrase_match
        if (keyword1 != null && !keyword1.isEmpty()) {
            QueryBuilder keywordQuery = QueryBuilders.matchPhraseQuery(field1, keyword1);
            searchQuery.should(keywordQuery);
        }
        if (keyword2 != null && !keyword2.isEmpty()) {
            QueryBuilder keywordQuery = QueryBuilders.matchPhraseQuery(field2, keyword2);
            searchQuery.should(keywordQuery);
        }

        return searchQuery;
    }

    private QueryBuilder buildQuery(String keyword, String field) {
        if (keyword == null || keyword.isEmpty())
            return null;

        BoolQueryBuilder retQuery = QueryBuilders.boolQuery();

        if (keyword != null) {
            QueryBuilder aQuery = buildContainOfQuery(keyword, field);
            if (aQuery != null)
                retQuery.must(aQuery);
        }

        return retQuery;
    }


    private QueryBuilder buildContainOfQuery(String keywords, String field) {
        if (keywords == null || keywords.isEmpty())
            return null;

        BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();
        searchQuery.minimumShouldMatch(1);    // Must match at least one phrase_match
        QueryBuilder keywordQuery = QueryBuilders.matchPhraseQuery(field, keywords.trim());
        searchQuery.should(keywordQuery);

        return searchQuery;
    }

    private QueryBuilder buildSingleKeywordQuery(String keyword, String filterField) {
        if (keyword == null || keyword.isEmpty()) return null;
        BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();
        QueryBuilder keywordQuery = QueryBuilders.termQuery(filterField, keyword.toUpperCase());
        searchQuery.should(keywordQuery);
        return searchQuery;
    }

    private QueryBuilder buildKeywordQuery(String keyword, String filterField) {
        if (keyword == null || keyword.isEmpty()) return null;
        BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();
        QueryBuilder keywordQuery = QueryBuilders.termQuery(filterField, keyword);
        searchQuery.should(keywordQuery);
        return searchQuery;
    }

    private QueryBuilder buildDateFilterQuery(String startDate, String endDate) {
        if (startDate == null && endDate == null)
            return null;

        QueryBuilder outQuery;
        if (startDate != null && endDate != null)
            outQuery = QueryBuilders.rangeQuery(Constant.DocumentFieldName.PUBLISHED_FIELD).gte(startDate).lt(endDate).format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        else if (endDate == null)
            outQuery = QueryBuilders.rangeQuery(Constant.DocumentFieldName.PUBLISHED_FIELD).gte(startDate);
        else
            outQuery = QueryBuilders.rangeQuery(Constant.DocumentFieldName.PUBLISHED_FIELD).lt(endDate);

        return outQuery;
    }

    public SearchResultPeople searchKeyword(String channels, String keyword, int page, int size, String language) {
        SearchResultPeople result = new SearchResultPeople();
        try {
            List<DocumentPeopleEntity> docList = new ArrayList<>();
            List<String> indexers = new ArrayList<>();
            indexers.add("ds-global-twitter*");
            indexers.add("ds-global-facebook*");
            indexers.add("ds-global-instagram*");

            if (channels != null && !channels.isEmpty()) {
                String[] arrUrls = channels.split(",");
                ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);
                List<Callable<Integer>> tasks = new LinkedList<>();
                for (String value : arrUrls) {
                    Callable<Integer> callableTask = () -> {
                        logger.info("Thread Id = {}", Thread.currentThread().getId());
                        Map<String, String> stringMap = convertUrlChannelsToList(value);
                        String domain = null;
                        String subDomain = null;
                        String category = null;
                        String subCategory = null;
                        if (stringMap != null) {
                            domain = stringMap.get("domain");
                            subDomain = stringMap.get("subDomain");
                            category = stringMap.get("category");
                            subCategory = stringMap.get("subCategory");
                        }
                        for (String indexName : indexers) {
                            List<DocumentPeopleEntity> documentEntityList = searchKeywordFromIndex(indexName, keyword, page, size, language, domain, subDomain, category, subCategory);
                            if (documentEntityList != null && documentEntityList.size() > 0) {
                                docList.addAll(documentEntityList);
                            }
                        }
                        return 1;
                    };
                    tasks.add(callableTask);
                }
                executorService.invokeAll(tasks);
                logger.info("shutdown thread search people");
                if (!executorService.isShutdown()) {
                    executorService.shutdown();
                }
            } else {
                for (String indexName : indexers) {
                    List<DocumentPeopleEntity> documentEntityList = searchKeywordFromIndex(indexName, keyword, page, size, language, null, null, null, null);
                    if (documentEntityList != null && documentEntityList.size() > 0) {
                        docList.addAll(documentEntityList);
                    }
                }
            }
            result.setData(docList);
            result.setResult("success");
            result.setResult_code(0);
        } catch (Exception e) {
            e.printStackTrace();
            result.setData(new ArrayList<>());
            result.setResult("faild");
            result.setResult_code(999);
        }
        return result;
    }


    public List<DocumentPeopleEntity> searchKeywordFromIndex(String indexName, String keyword, int page, int size, String language, String domainUrl, String subdomainUrl, String categoryUrl, String subcategoryUrl) {

        if (indexName == null) return null;
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        QueryBuilder languageQuery = buildSingleKeywordQuery(language, Constant.DocumentFieldName.LANGUAGE_FIELD);
        QueryBuilder keywordQuery = buildContainOneOfQuery(keyword, Constant.DocumentFieldName.SEARCH_CONTENT_FIELD);

        QueryBuilder domainUrlQuery = null;
        QueryBuilder subDomainUrlQuery = null;
        //  QueryBuilder categoryUrlQuery = null;
        QueryBuilder subCategoryUrlQuery = null;
        if (domainUrl != null) {
            domainUrlQuery = buildQuery(domainUrl.trim().toLowerCase(), Constant.DocumentFieldName.DOMAIN_URL_FIELD);
        }
        if (subdomainUrl != null && categoryUrl != null) {
            subDomainUrlQuery = buildContainOneOfQuery(subdomainUrl.trim().toLowerCase(), categoryUrl.trim().toLowerCase(),
                    Constant.DocumentFieldName.SUB_DOMAIN_URL_FIELD, Constant.DocumentFieldName.CATEGORY_FIELD);
        }

        if (subcategoryUrl != null) {
            subCategoryUrlQuery = buildQuery(subcategoryUrl.trim().toLowerCase(), Constant.DocumentFieldName.SUB_CATEGORY_FIELD);
        }

        if (languageQuery != null)
            boolQuery.must(languageQuery);

        if (keywordQuery != null) {
            boolQuery.must(keywordQuery);
        }

        if (domainUrlQuery != null) {
            boolQuery.must(domainUrlQuery);
        }

        if (subDomainUrlQuery != null) {
            boolQuery.must(subDomainUrlQuery);
        }

        if (subCategoryUrlQuery != null) {
            boolQuery.must(subCategoryUrlQuery);
        }

        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQuery);
        int from = (page - 1) * size;
        searchSourceBuilder.from(from).size(size);

        searchSourceBuilder.sort(Constant.DocumentFieldName.PUBLISHED_FIELD, SortOrder.DESC);

        searchSourceBuilder.storedFields(listObject);
        searchSourceBuilder.query(boolQuery);
        searchSourceBuilder.trackTotalHits(true);
        searchRequest.source(searchSourceBuilder);
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());

        SearchResponse searchResponse = null;
        try {
            searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHits hits = searchResponse.getHits();
        Iterator<SearchHit> iter = hits.iterator();
        List<DocumentPeopleEntity> documentEntityList = new ArrayList<>();
        while (iter.hasNext()) {
            DocumentPeopleEntity doc = new DocumentPeopleEntity();
            SearchHit hit = iter.next();
            try {
                DocumentPeopleEntity documentEntity = doc.convertObjectToDoc(hit);
                if (documentEntity != null) {
                    documentEntityList.add(documentEntity);
                }
            } catch (Exception e) {
                logger.error("Error content: " + e.getMessage());
            }

        }
        return documentEntityList;
    }

    public SearchResult searchContent(String keyword, int page, String searchType, String language, String fromDate, String toDate, Integer size, List<String> authors, String channels, String sortBy, String reverseSort,
                                      Integer score, String from, String util) {
        SearchResult result = new SearchResult();
        try {
            List<DocumentEntity> docList = new ArrayList<>();
            List<String> indexers = new ArrayList<>();
            indexers.add("ds-global-news*");
            indexers.add("ds-global-video*");
            indexers.add("ds-global-pdf*");
            indexers.add("ds-global-ppt*");
            indexers.add("ds-global-doc*");
            indexers.add("ds-global-dart*");
            indexers.add("ds-global-investing*");
            indexers.add("ds-global-twitter*");
            indexers.add("ds-global-linkedin*");
            indexers.add("ds-global-facebook*");
            indexers.add("ds-global-blog*");
            indexers.add("ds-global-youtube*");
            indexers.add("ds-global-instagram*");

            if (channels != null && !channels.isEmpty()) {

                String[] arrUrls = channels.split(",");
                ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);
                List<Callable<Integer>> tasks = new LinkedList<>();
                for (String value : arrUrls) {
                    Callable<Integer> callableTask = () -> {
                        logger.info("Thread Id = {}", Thread.currentThread().getId());
                        Map<String, String> stringMap = convertUrlChannelsToList(value);
                        String domain = null;
                        String subDomain = null;
                        String category = null;
                        String subCategory = null;
                        if (stringMap != null) {
                            domain = stringMap.get("domain");
                            subDomain = stringMap.get("subDomain");
                            category = stringMap.get("category");
                            subCategory = stringMap.get("subCategory");
                        }
                        for (String indexName : indexers) {
                            List<DocumentEntity> documentEntityList = searchContentFromIndex(indexName, keyword, page, searchType, language, fromDate, toDate, size, authors, domain, subDomain, category, subCategory, sortBy, reverseSort,
                                    score, from, util);
                            if (documentEntityList != null && documentEntityList.size() > 0) {
                                docList.addAll(documentEntityList);
                            }
                        }
                        return 1;
                    };
                    tasks.add(callableTask);
                }
                executorService.invokeAll(tasks);
                if (!executorService.isShutdown()) {
                    executorService.shutdown();
                }
                logger.info("Thread shutdown");
            } else {
                for (String indexName : indexers) {
                    List<DocumentEntity> documentEntityList = searchContentFromIndex(indexName, keyword, page, searchType, language, fromDate, toDate, size, authors, null, null, null, null, sortBy, reverseSort,
                            score, from, util);
                    if (documentEntityList != null && documentEntityList.size() > 0) {
                        docList.addAll(documentEntityList);
                    }
                }
            }
            result.setData(docList);
            result.setResult("success");
            result.setResult_code(0);
        } catch (Exception e) {
            e.printStackTrace();
            result.setData(new ArrayList<>());
            result.setResult("faild");
            result.setResult_code(999);
        }
        return result;
    }

    public List<DocumentEntity> searchContentFromIndex(String indexName, String keyword, int page, String searchType, String language, String fromDate, String toDate, Integer size, List<String> authors,
                                                       String domainUrl, String subdomainUrl, String categoryUrl, String subcategoryUrl, String sortBy, String reverseSort, Integer score, String from,
                                                       String util) {

        if (indexName == null) return null;

        String type = searchType(indexName);

        if (type == null) return null;

        if (searchType == null) searchType = type;
        else {
            if (searchType.equalsIgnoreCase("Social")) {
                if (!type.equalsIgnoreCase("Dart") && !type.equalsIgnoreCase("Investing") && !type.equalsIgnoreCase("Twitter")
                        && !type.equalsIgnoreCase("Linkedin") && !type.equalsIgnoreCase("Facebook")) return null;
            } else {
                if (!searchType.equalsIgnoreCase(type)) return null;
            }

        }

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        QueryBuilder keywordQuery = buildContainQuery(keyword, Constant.DocumentFieldName.SEARCH_CONTENT_FIELD);
        QueryBuilder languageQuery = buildSingleKeywordQuery(language, Constant.DocumentFieldName.LANGUAGE_FIELD);
        QueryBuilder dateQuery = buildDateFilterQuery(fromDate, toDate);
        QueryBuilder dateSearchQuery = buildDateFilterQuery(from, util);
        QueryBuilder authorQuery = buildContainOneOfQuery(authors, Constant.DocumentFieldName.WRITER_SEARCH_FIELD);
        QueryBuilder domainUrlQuery = null;
        QueryBuilder subDomainUrlQuery = null;
        //  QueryBuilder categoryUrlQuery = null;
        QueryBuilder subCategoryUrlQuery = null;
        if (domainUrl != null) {
            domainUrlQuery = buildQuery(domainUrl.trim().toLowerCase(), Constant.DocumentFieldName.DOMAIN_URL_FIELD);
        }
        if (subdomainUrl != null && categoryUrl != null) {
            subDomainUrlQuery = buildContainOneOfQuery(subdomainUrl.trim().toLowerCase(), categoryUrl.trim().toLowerCase(),
                    Constant.DocumentFieldName.SUB_DOMAIN_URL_FIELD, Constant.DocumentFieldName.CATEGORY_FIELD);
        }

        if (subcategoryUrl != null) {
            subCategoryUrlQuery = buildQuery(subcategoryUrl.trim().toLowerCase(), Constant.DocumentFieldName.SUB_CATEGORY_FIELD);
        }
        if (keywordQuery != null)
            boolQuery.must(keywordQuery);

        if (languageQuery != null)
            boolQuery.must(languageQuery);

        if (dateQuery != null)
            boolQuery.must(dateQuery);

        if (dateSearchQuery != null)
            boolQuery.must(dateSearchQuery);

        if (authorQuery != null) {
            boolQuery.must(authorQuery);
        }

        if (domainUrlQuery != null) {
            boolQuery.must(domainUrlQuery);
        }

        if (subDomainUrlQuery != null) {
            boolQuery.must(subDomainUrlQuery);
        }

//        if(categoryUrlQuery != null){
//            boolQuery.must(categoryUrlQuery);
//        }

        if (subCategoryUrlQuery != null) {
            boolQuery.must(subCategoryUrlQuery);
        }

        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQuery);
        int fromPage = (page - 1) * size;
        searchSourceBuilder.from(fromPage).size(size);

        String[] sorts = sortBy.split(",");
        for (String sort : sorts) {

            if (sort.equalsIgnoreCase("date")) {
                if (reverseSort.equalsIgnoreCase("false")) {
                    searchSourceBuilder.sort(Constant.DocumentFieldName.PUBLISHED_FIELD, SortOrder.DESC);
                } else if (reverseSort.equalsIgnoreCase("true")) {
                    searchSourceBuilder.sort(Constant.DocumentFieldName.PUBLISHED_FIELD, SortOrder.ASC);
                }
            } else if (sort.equalsIgnoreCase("score")) {
                if (reverseSort.equalsIgnoreCase("false")) {
                    searchSourceBuilder.sort("_score", SortOrder.DESC);
                } else if (reverseSort.equalsIgnoreCase("true")) {
                    searchSourceBuilder.sort("_score", SortOrder.ASC);
                }
            }

        }

        searchSourceBuilder.storedFields(listObject);
        searchSourceBuilder.query(boolQuery);
        searchSourceBuilder.trackTotalHits(true);
        searchSourceBuilder.trackScores(true);
        searchRequest.source(searchSourceBuilder);
        if (score != null) {
            searchRequest.source().minScore(score);
        }

        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());


        SearchResponse searchResponse = null;
        try {
            searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHits hits = searchResponse.getHits();
        Iterator<SearchHit> iter = hits.iterator();
        List<DocumentEntity> documentEntityList = new ArrayList<>();
        while (iter.hasNext()) {
            DocumentEntity doc = new DocumentEntity();
            SearchHit hit = iter.next();
            try {
                DocumentEntity documentEntity = doc.covertObjectToDoc(hit, searchType, keyword, type);
                if (documentEntity != null) {
                    documentEntityList.add(documentEntity);
                }
            } catch (Exception e) {
                logger.error("Error content: " + e.getMessage());
            }

        }
        return documentEntityList;
    }

    public SearchHits getListFeed(String connectomeId, Integer page, Integer size) {
        try {
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

            MatchPhraseQueryBuilder queryBuilder = new MatchPhraseQueryBuilder(Constant.DocumentFieldName.CONNECTOME_ID_FIELD, connectomeId);
            boolQueryBuilder.must(queryBuilder);

            MatchPhraseQueryBuilder queryIsNotDeleted = new MatchPhraseQueryBuilder(Constant.DocumentFieldName.IS_DELETED_FIELD, "false");
            boolQueryBuilder.filter(queryIsNotDeleted);

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.from((page) * size);
            sourceBuilder.size(size);
            sourceBuilder.query(boolQueryBuilder);
            sourceBuilder.storedFields(FeedModel.getStoreFields());
            sourceBuilder.sort(Constant.DocumentFieldName.CREATED_DATE_FIELD, SortOrder.DESC);

            SearchRequest searchRequest = new SearchRequest().source(sourceBuilder);
            searchRequest.indices("ds-feed*");

            SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);

            return searchResponse.getHits();

        } catch (IOException e) {
            logger.error("query get list feed failed, exception: " + e.getMessage());
            return null;
        }
    }

//    public SearchHits searchFeedContent(String keyword, Integer page, Integer size, String[] indexes) {
////
////        try {
////            MatchQueryBuilder queryBuilder = null;
////
////            if (keyword != null) {
////                queryBuilder = new MatchQueryBuilder("content_search", keyword);
////            }
////
////            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
////            sourceBuilder.from((page - 1) * size);
////            sourceBuilder.size(size);
////            sourceBuilder.query(queryBuilder);
////
////            SearchRequest searchRequest = new SearchRequest(indexes).source(sourceBuilder);
////
////            SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
////
////            return searchResponse.getHits();
////        } catch (Exception e) {
////            logger.error("query search feed failed, exception: " + e.getMessage());
////            return null;
////        }
////    }

    public SearchHits getFeedContent(String docId, String[] indexes) {
        try {

            MatchPhraseQueryBuilder queryBuilder = new MatchPhraseQueryBuilder("_id", docId);

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(queryBuilder);
            sourceBuilder.storedFields(DocContentModel.getStoreFields());

            SearchRequest searchRequest = new SearchRequest(indexes).source(sourceBuilder);
//            searchRequest.indices(indexes);

            SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);

            return searchResponse.getHits();

        } catch (IOException e) {
            logger.error("query get feed content failed, exception: " + e.getMessage());
            return null;
        }
    }

    public SearchHits getFeed(String docId, String connectomeId) {
        try {

            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

            MatchPhraseQueryBuilder matchQueryBuilder = new MatchPhraseQueryBuilder(Constant.DocumentFieldName.DOC_ID_CONTENT_FIELD, docId);
            boolQueryBuilder.must(matchQueryBuilder);

            MatchPhraseQueryBuilder connectomeQueryBuilder = new MatchPhraseQueryBuilder(Constant.DocumentFieldName.CONNECTOME_ID_FIELD, connectomeId);
            boolQueryBuilder.must(connectomeQueryBuilder);

            MatchPhraseQueryBuilder queryIsNotDeleted = new MatchPhraseQueryBuilder(Constant.DocumentFieldName.IS_DELETED_FIELD, "false");
            boolQueryBuilder.filter(queryIsNotDeleted);

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(boolQueryBuilder);
            sourceBuilder.storedFields(FeedModel.getStoreFields());

            SearchRequest searchRequest = new SearchRequest().source(sourceBuilder);
            searchRequest.indices("ds-feed*");

            SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);

            return searchResponse.getHits();

        } catch (IOException e) {
            logger.error("query get feed failed, exception: " + e.getMessage());
            return null;
        }

    }

    public SearchHits getListFeedContent(List<String> contentIds) {
        try {

            Set<String> docIds = Sets.newHashSet(contentIds);

            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            BoolQueryBuilder docIdQueryBuilder = new BoolQueryBuilder();

            for (String docId : docIds) {
                MatchPhraseQueryBuilder queryBuilder = new MatchPhraseQueryBuilder("_id", docId);
                docIdQueryBuilder.should(queryBuilder);
            }

            boolQueryBuilder.must(docIdQueryBuilder);

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.trackTotalHits(true);
            sourceBuilder.query(boolQueryBuilder);
            sourceBuilder.storedFields(DocContentModel.getStoreFields());

//            SearchRequest searchRequest = new SearchRequest(FeedContentModel.getListIndexByDocIds(contentIds)).source(sourceBuilder);
            SearchRequest searchRequest = new SearchRequest(getTempIndexes()).source(sourceBuilder);
            SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);

            return searchResponse.getHits();

        } catch (IOException e) {
            logger.error("query get list document content failed, exception: " + e.getMessage());
            return null;
        }
    }

    private String[] getTempIndexes() {
        String[] indexes = new String[2];
        indexes[0] = "ds-global*";
        indexes[1] = "ds-private*";
        return indexes;
    }

    public SearchHits searchListFeed(String connectomeId, String keyword, Date fromDate, Date untilDate, Integer page, Integer size, String searchType, String channel, String lang, String[] indexes, String isFeed, String sortBy, Float score, List<String> writers) {
        try {

            MatchPhraseQueryBuilder queryBuilder = new MatchPhraseQueryBuilder(Constant.DocumentFieldName.CONNECTOME_ID_FIELD, connectomeId);

            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            boolQueryBuilder.must(queryBuilder);

            if (fromDate != null || untilDate != null) {
                RangeQueryBuilder queryDateBuilder = new RangeQueryBuilder(Constant.DocumentFieldName.PUBLISHED_FIELD);

                if (fromDate != null) {
                    queryDateBuilder.from(fromDate);
                }

                if (untilDate != null) {
                    queryDateBuilder.to(untilDate);
                }
                boolQueryBuilder.filter(queryDateBuilder);
            }

            if (channel != null) {
                MatchQueryBuilder queryChannelsBuilder = new MatchQueryBuilder(Constant.DocumentFieldName.CHANNEL_FIELD, channel.toUpperCase());
                boolQueryBuilder.filter(queryChannelsBuilder);
            }

            if (keyword != null) {
//                QueryStringQueryBuilder queryKeywordBuilder = new QueryStringQueryBuilder("*" + keyword + "*");
//                queryKeywordBuilder.defaultField(Constant.DocumentFieldName.SEARCH_CONTENT_FIELD);
//                boolQueryBuilder.must(queryKeywordBuilder);

                MatchPhrasePrefixQueryBuilder queryChannelsBuilder = new MatchPhrasePrefixQueryBuilder(Constant.DocumentFieldName.SEARCH_CONTENT_FIELD, keyword);
                boolQueryBuilder.must(queryChannelsBuilder);
            }

            if (isFeed != null && isFeed.equalsIgnoreCase("true")) {
                MatchPhraseQueryBuilder queryIsFeedBuilder = new MatchPhraseQueryBuilder(Constant.DocumentFieldName.IS_FEED_FIELD, isFeed.toLowerCase());
                boolQueryBuilder.filter(queryIsFeedBuilder);
            }

            if (searchType != null) {
                if (searchType.equalsIgnoreCase("document")) {

                    BoolQueryBuilder querySearchTypeDocBuilder = new BoolQueryBuilder();

                    MatchPhraseQueryBuilder tempPDFQuery = new MatchPhraseQueryBuilder(Constant.DocumentFieldName.SEARCH_TYPE, "PDF");
                    querySearchTypeDocBuilder.should(tempPDFQuery);
                    MatchPhraseQueryBuilder tempPPTQuery = new MatchPhraseQueryBuilder(Constant.DocumentFieldName.SEARCH_TYPE, "PPT");
                    querySearchTypeDocBuilder.should(tempPPTQuery);

                    boolQueryBuilder.filter(querySearchTypeDocBuilder);
                } else {
                    MatchPhraseQueryBuilder querySearchTypeBuilder = new MatchPhraseQueryBuilder(Constant.DocumentFieldName.SEARCH_TYPE, searchType.toUpperCase());
                    boolQueryBuilder.filter(querySearchTypeBuilder);
                }
            }

            if (lang != null) {
                MatchPhraseQueryBuilder queryLangBuilder = new MatchPhraseQueryBuilder(Constant.DocumentFieldName.LANGUAGE_FIELD, lang);
                boolQueryBuilder.filter(queryLangBuilder);
            }

            if(writers != null){
                BoolQueryBuilder writersQueryBuilder = new BoolQueryBuilder();

                for (String writer : writers){
                    MatchPhrasePrefixQueryBuilder writerQueryBuilder = new MatchPhrasePrefixQueryBuilder(Constant.DocumentFieldName.WRITER_SEARCH_FIELD, writer);
                    writersQueryBuilder.should(writerQueryBuilder);
                }

                boolQueryBuilder.must(writersQueryBuilder);
            }

            MatchPhraseQueryBuilder queryIsNotDeleted = new MatchPhraseQueryBuilder(Constant.DocumentFieldName.IS_DELETED_FIELD, "false");
            boolQueryBuilder.filter(queryIsNotDeleted);

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.from((page) * size);
            sourceBuilder.size(size);
            sourceBuilder.query(boolQueryBuilder);
            sourceBuilder.storedFields(FeedModel.getStoreFields());
            sourceBuilder.trackTotalHits(true);
            if (sortBy.equalsIgnoreCase("date")) {
                sourceBuilder.sort(Constant.DocumentFieldName.CREATED_DATE_FIELD, SortOrder.DESC);
            }
            if(score != null){
                sourceBuilder.minScore(score);
            }

            SearchRequest searchRequest = new SearchRequest(indexes).source(sourceBuilder);
//            searchRequest.indices("ds-feed*");

            SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);

            return searchResponse.getHits();

        } catch (IOException e) {
            logger.error("query get list feed failed, exception: " + e.getMessage());
            return null;
        }
    }

    public SearchHits getListDocumentByIds(Set<String> docIds, String connectomeId, Integer page, Integer size) {
        try {
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

            BoolQueryBuilder boolQueryBuilderByDocIds = new BoolQueryBuilder();

            for (String docId : docIds) {
                MatchPhraseQueryBuilder queryIdBuilder = new MatchPhraseQueryBuilder(Constant.DocumentFieldName.DOC_ID_CONTENT_FIELD, docId.trim());
                boolQueryBuilderByDocIds.should(queryIdBuilder);
            }

            MatchQueryBuilder connectomeIdQueryBuilder = new MatchQueryBuilder(Constant.DocumentFieldName.CONNECTOME_ID_FIELD, connectomeId);
            boolQueryBuilder.filter(connectomeIdQueryBuilder);

            boolQueryBuilder.must(boolQueryBuilderByDocIds);

            MatchPhraseQueryBuilder queryIsNotDeleted = new MatchPhraseQueryBuilder(Constant.DocumentFieldName.IS_DELETED_FIELD, "false");
            boolQueryBuilder.filter(queryIsNotDeleted);

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            if (size != null && size == 0) {
                size = 10;
            }

            if (page != null && size != null) {
                sourceBuilder.from((page) * size);
                sourceBuilder.size(size);
            }

            sourceBuilder.trackTotalHits(true);
            sourceBuilder.query(boolQueryBuilder);
            sourceBuilder.storedFields(FeedModel.getStoreFields());
            sourceBuilder.sort(Constant.DocumentFieldName.CREATED_DATE_FIELD, SortOrder.DESC);

            SearchRequest searchRequest = new SearchRequest().source(sourceBuilder);
            searchRequest.indices("ds-feed*");

            SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);

            return searchResponse.getHits();

        } catch (IOException e) {
            logger.error("query get list feed failed, exception: " + e.getMessage());
            return null;
        }
    }

    public SearchHits getListFilterFeed(String connectomeId, Integer page, Integer size, String type) {
        try {
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

            MatchPhraseQueryBuilder queryBuilder = new MatchPhraseQueryBuilder(Constant.DocumentFieldName.CONNECTOME_ID_FIELD, connectomeId);
            boolQueryBuilder.must(queryBuilder);

            if (type != null) {
                filterType(boolQueryBuilder, type);
            }

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.from((page) * size);
            sourceBuilder.size(size);
            sourceBuilder.query(boolQueryBuilder);
            sourceBuilder.storedFields(FeedModel.getStoreFields());
            sourceBuilder.sort(Constant.DocumentFieldName.CREATED_DATE_FIELD, SortOrder.DESC);

            SearchRequest searchRequest = new SearchRequest().source(sourceBuilder);
            searchRequest.indices("ds-feed*");

            SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);

            return searchResponse.getHits();

        } catch (IOException e) {
            logger.error("query get list feed failed, exception: " + e.getMessage());
            return null;
        }
    }

    private void filterType(BoolQueryBuilder boolQueryBuilder, String type) {
        MatchPhraseQueryBuilder queryIsNotDeleted = new MatchPhraseQueryBuilder(Constant.DocumentFieldName.IS_DELETED_FIELD, false);
        switch (type) {
            case "liked":
                MatchQueryBuilder likedQueryBuilder = new MatchQueryBuilder(Constant.DocumentFieldName.LIKED_FIELD, 1);
                boolQueryBuilder.filter(likedQueryBuilder);
                boolQueryBuilder.filter(queryIsNotDeleted);
                break;
            case "disliked":
                MatchQueryBuilder dislikeQueryBuilder = new MatchQueryBuilder(Constant.DocumentFieldName.LIKED_FIELD, 2);
                boolQueryBuilder.filter(dislikeQueryBuilder);
                boolQueryBuilder.filter(queryIsNotDeleted);
                break;
            case "bookmarked":
                MatchQueryBuilder bookmarkQuerybuilder = new MatchQueryBuilder(Constant.DocumentFieldName.IS_BOOKMARK_FIELD, true);
                boolQueryBuilder.filter(bookmarkQuerybuilder);
                boolQueryBuilder.filter(queryIsNotDeleted);
                break;
            case "hidden":
                MatchQueryBuilder isDeletedQuerybuilder = new MatchQueryBuilder(Constant.DocumentFieldName.IS_DELETED_FIELD, true);
                boolQueryBuilder.filter(isDeletedQuerybuilder);
                break;
            default:
                boolQueryBuilder.filter(queryIsNotDeleted);
                break;
        }
    }

    public SearchHits getDocumentById(String docId) {
        try {
            MatchPhraseQueryBuilder queryBuilder = new MatchPhraseQueryBuilder("_id", docId);

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(queryBuilder);
            sourceBuilder.storedFields(DocContentModel.getStoreFields());
            sourceBuilder.sort(Constant.DocumentFieldName.CREATED_DATE_FIELD, SortOrder.DESC);

            SearchRequest searchRequest = new SearchRequest(DocContentModel.getIndexByDocId(docId)).source(sourceBuilder);

            SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);

            return searchResponse.getHits();

        } catch (IOException e) {
            logger.error("query get list feed failed, exception: " + e.getMessage());
            return null;
        }
    }
}
