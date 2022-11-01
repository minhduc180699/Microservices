package com.ds.dssearcher.service;


import com.ds.dssearcher.entity.*;
import com.ds.dssearcher.model.*;
import com.ds.dssearcher.repository.EntityLinkingRepository;
import com.ds.dssearcher.repository.SearchRepository;
import com.ds.dssearcher.response.ResponseDocument;
import com.ds.dssearcher.response.ResponseListData;
import com.ds.dssearcher.util.DateUtil;
import com.google.common.collect.Sets;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

@Service
public class SearchServiceImpl implements ISearchSevice {


    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private EntityLinkingRepository entityLinkingRepository;

    @Override
    public DetailDocumentEntity getById(String id) {
        return searchRepository.getById(id);
    }

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private static final DateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static final DateFormat formatter = new SimpleDateFormat("yyyy");
    private static final DateFormat simpleMonthFormat = new SimpleDateFormat("MM_yyyy");

    private static final ExecutorService executorService = Executors.newFixedThreadPool(20);

    public ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public SearchResult searchKeyword(String keyWord, int page, String searchType, String language, String searchDate, Integer size, List<String> authors, String channels, String sortBy, String reverseSort,
                                      Integer score, String from, String util, String entityLinkingType) throws Exception {
        List<String> dates = convertDate(searchDate);
        String fromDate = null;
        String toDate = null;

        if (dates != null) {
            fromDate = dates.get(0);
            toDate = dates.get(1);
        }

        SearchResult searchResult = searchRepository.searchKeyword(keyWord, page, searchType, LanguageEnum.getLanguage(language), fromDate, toDate, size, authors, channels, sortBy, reverseSort, score,
                DateUtil.getCurrentDateToString(DateUtil.getStringToDate(from)), DateUtil.getCurrentDateToString(DateUtil.getStringToDate(util)));

        for (DocumentEntity documentEntity : searchResult.getData()) {
            EntityLinking responseEntityLinking = getEntityLinking(entityLinkingType, documentEntity.get_id());
            if (responseEntityLinking == null) continue;
            if (!entityLinkingType.equalsIgnoreCase("summary"))
                documentEntity.setEntityLinking(responseEntityLinking.getResponseEntityLinking());
            if (!entityLinkingType.equalsIgnoreCase("full"))
                documentEntity.setEntityLinking_summary(responseEntityLinking.getResponseEntityLinkingSummary());
        }
        return searchResult;
    }

    private EntityLinking getEntityLinking(String entityLinkingType, String docId) {
        EntityLinking responseEntityLinking = null;

//        if(entityLinkingType.equalsIgnoreCase("all")){
//            responseEntityLinking=  entityLinkingRepository.findByDocId(docId);
//        }else if(entityLinkingType.equalsIgnoreCase("full")){
//            responseEntityLinking=  entityLinkingRepository.findByDocIdAndResponseEntityLinkingExists(docId,true);
//        }else if(entityLinkingType.equalsIgnoreCase("summary")){
//            responseEntityLinking=  entityLinkingRepository.findByDocIdAndResponseEntityLinkingSummaryExists(docId,true);
//        }
        responseEntityLinking = entityLinkingRepository.findByDocId(docId);
        return responseEntityLinking;
    }

    @Override
    public SearchResultPeople searchPeople(String channels, List<String> authors, List<String> authorIds, int page, int size, String language) {
        return searchRepository.searchPeople(channels, authors, authorIds, page, size, LanguageEnum.getLanguage(language));
    }

    @Override
    public SearchResultDataSource searchSourceData(String dataSourceIds, String channels, int page, int size, String serviceType, String lang) {
        SearchResultDataSource dataSource = searchRepository.searchDataOfDatasources(dataSourceIds, channels, page, size, serviceType, lang);
        return dataSource;
    }

    @Override
    public ResultCountDatasource countDataSourceById(String dataSourceIds, String channels, String serviceType, String lang) {
        return searchRepository.countDataSourceById(dataSourceIds, channels, serviceType, lang);
    }

    @Override
    public SearchResultPeople searchKeyword(String channels, String keyword, int page, int size, String language) {
        return searchRepository.searchKeyword(channels, keyword, page, size, LanguageEnum.getLanguage(language));
    }

    @Override
    public SearchResult searchContent(String keyWord, int page, String searchType, String language, String searchDate, Integer size, List<String> authors, String channels, String sortBy, String reverseSort, Integer score, String from, String util) throws Exception {
        List<String> dates = convertDate(searchDate);
        String fromDate = null;
        String toDate = null;

        if (dates != null) {
            fromDate = dates.get(0);
            toDate = dates.get(1);
        }
        return searchRepository.searchContent(keyWord, page, searchType, LanguageEnum.getLanguage(language), fromDate, toDate, size, authors, channels, sortBy, reverseSort, score,
                DateUtil.getCurrentDateToString(DateUtil.getStringToDate(from)), DateUtil.getCurrentDateToString(DateUtil.getStringToDate(util)));
    }

    @Override
    public ResponseListData<FeedModel> getListFeed(String connectomeId, Integer page, Integer size) {

        ResponseListData<FeedModel> responseListData = new ResponseListData<>();

        SearchHits searchHits = searchRepository.getListFeed(connectomeId, page, size);

        Iterator<SearchHit> iter = searchHits.iterator();
        List<FeedModel> feedDocuments = new ArrayList<>();

        while (iter.hasNext()) {
            SearchHit hit = iter.next();
            try {
                FeedModel feedModel = FeedModel.convertToFeedDocument(hit);

                if (feedModel != null) {
                    feedDocuments.add(feedModel);
                } else {
                    //Todo nothing
                }

            } catch (Exception e) {
                logger.error("Error content in get list feed, " + e.getMessage());
            }

        }
        responseListData.setTotalPages(getTotalPage(searchHits.getTotalHits().value, size));
        responseListData.setTotalItems(searchHits.getTotalHits().value);
        responseListData.setCurrentPage(page);
        responseListData.setData(feedDocuments);
        return responseListData;
    }

    @Override
    public ResponseListData<FeedModel> searchFeed(String connectomeId, String keyword, String from, String until, String searchType, Integer page, Integer size, String channels, String lang, String type, String sortBy, Float score, List<String> writer) {
        String isFeed = null;
        if (type.equalsIgnoreCase("feed")) {
            isFeed = "true";
        } else if (type.equalsIgnoreCase("PERSONAL_DOCUMENT")) {
            isFeed = "none";
        } else return null;

        if (lang.toLowerCase().equals("en")) {
            lang = "ENGLISH";
        } else if (lang.toLowerCase().equals("ko")) {
            lang = "KOREA";
        } else {
            return null;
        }

        Date fromDate = convertStringToDate(from);
        Date untilDate = convertStringToDate(until);

        String[] indexes = null;
        if (from != null && until != null) {
            indexes = findIndex(from, until);
        } else if (from != null) {
            until = DateTimeFormatter.ofPattern("MM/dd/yyyy").format(LocalDate.now()).toString();
            indexes = findIndex(from, until);
        } else if (until != null) {
            logger.error("fromDate null !!!");
            return null;
        }
        // from null and until null
        else {
            indexes = findIndex(searchType);
        }

        if (!sortBy.equalsIgnoreCase("date") && !sortBy.equalsIgnoreCase("score")){
            return null;
        }

        return searchListFeed(connectomeId, keyword, fromDate, untilDate, page, size, searchType, channels, lang, indexes, isFeed, sortBy, score, writer);
    }


    private ResponseListData<FeedModel> searchListFeed(String connectomeId, String keyword, Date fromDate, Date untilDate, Integer page, Integer size, String searchType, String channels, String lang, String[] indexes, String isFeed, String sortBy, Float score, List<String> writer) {

        ResponseListData<FeedModel> responseListData = new ResponseListData<>();

        SearchHits searchHits = searchRepository.searchListFeed(connectomeId, keyword, fromDate, untilDate, page, size, searchType, channels, lang, indexes, isFeed, sortBy, score, writer);

        Iterator<SearchHit> iter = searchHits.iterator();
        List<FeedModel> feedDocuments = new ArrayList<>();

        while (iter.hasNext()) {
            SearchHit hit = iter.next();
            try {
                FeedModel feedModel = FeedModel.convertToFeedDocument(hit);

                if (feedModel != null) {
                    feedDocuments.add(feedModel);
                } else {
                    //Todo nothing
                }

            } catch (Exception e) {
                logger.error("Error content in get list feed, " + e.getMessage());
            }

        }

        responseListData.setTotalPages(getTotalPage(searchHits.getTotalHits().value, size));
        responseListData.setTotalItems(searchHits.getTotalHits().value);
        responseListData.setCurrentPage(page);
        responseListData.setData(feedDocuments);
        return responseListData;
    }

    private String[] findIndex(String fromDate, String untilDate) {


        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        Calendar defaultTime = Calendar.getInstance();

        try {
            beginCalendar.setTime(formatter.parse(fromDate.substring(6)));
            finishCalendar.setTime(formatter.parse(untilDate.substring(6)));
            defaultTime.setTime(formatter.parse("2022"));
        } catch (ParseException e) {
            logger.error("can not parse String to date: " + e.getMessage());
            return null;
        }

        Set<String> indexByYearList = new HashSet<>();

        if (beginCalendar.before(defaultTime) || finishCalendar.before(defaultTime)) {
            indexByYearList.add("ds-feed-*");
            String[] indexes = new String[indexByYearList.size()];
            return indexByYearList.toArray(indexes);
        } else {
            while (beginCalendar.before(finishCalendar)) {
                indexByYearList.add("ds-feed-" + "*" + formatter.format(beginCalendar.getTime()).toUpperCase());
                beginCalendar.add(Calendar.YEAR, 1);
            }
            indexByYearList.add("ds-feed-" + "*" + formatter.format(beginCalendar.getTime()).toUpperCase());
            String[] indexes = new String[indexByYearList.size()];
            return indexByYearList.toArray(indexes);
        }
//        Set<String> indexList = new HashSet<>();
//        Set<String> tempListIndex = new HashSet<>();
//        while (beginCalendar.before(finishCalendar)) {
//            // add one month to date per loop
//            if (beginCalendar.get(Calendar.MONTH) == Calendar.JANUARY) {
//                while (beginCalendar.before(finishCalendar)) {
//                    addIndexToList(tempListIndex, indexByYearList, beginCalendar, searchType);
//                    beginCalendar.add(Calendar.MONTH, 1);
//                }
//                addIndexToList(tempListIndex, indexByYearList, beginCalendar, searchType);
//            } else {
//                indexList.add("ds-feed-" + simpleMonthFormat.format(beginCalendar.getTime()).toUpperCase());
//            }
//            beginCalendar.add(Calendar.MONTH, 1);
//        }
//        indexList.add("ds-feed-" + simpleMonthFormat.format(beginCalendar.getTime()).toUpperCase());
//
//        if (indexByYearList.size() != 0) {
//            indexList.addAll(indexByYearList);
//        }
//        if (tempListIndex.size() != 0) {
//            indexList.addAll(tempListIndex);
//        }
//         String[] indexes = new String[indexList.size()];
//        return indexList.toArray(indexes);
    }

//    private void addIndexToList(Set<String> tempListIndex, Set<String> indexByYearList, Calendar beginCalendar, String searchType) {
//        tempListIndex.add("ds-feed-" + simpleMonthFormat.format(beginCalendar.getTime()).toUpperCase());
//        if (tempListIndex.size() >= 12) {
//            indexByYearList.add("ds-feed-" + "*" + beginCalendar.get(Calendar.YEAR));
//            tempListIndex.clear();
//        }
//    }

    private String[] findIndex(String searchType) {
        String[] indexes = new String[1];
        indexes[0] = "ds-feed-" + "*";
        return indexes;
    }

    @Override
    public DocContentModel getFeedContent(String feedId) {
        String indexes = findIndexByFeedId(feedId);
        String[] tempIndexes = getTempIndexes();

        SearchHits searchHits = searchRepository.getFeedContent(feedId, tempIndexes);

        Iterator<SearchHit> iter = searchHits.iterator();

        Map<String, DocContentModel> feedContentModelMap = new HashMap<>();

        while (iter.hasNext()) {
            SearchHit hit = iter.next();
            try {
                DocContentModel docContentModel = DocContentModel.convertToFeedDocument(hit);
                try {
                    if (feedContentModelMap.get(feedId) == null || docContentModel.getContent() != null) {
                        feedContentModelMap.put(feedId, docContentModel);
                    }
                } catch (NullPointerException e) {
                    logger.error(String.format("[DocId: %s] content null", docContentModel));
                }
            } catch (Exception e) {
                logger.error("Error content in search feed, " + e.getMessage());
                return null;
            }
        }
        return feedContentModelMap.get(feedId);
    }

    private String[] getTempIndexes() {
        String[] indexes = new String[2];
        indexes[0] = "ds-global*";
        indexes[1] = "ds-private*";
        return indexes;
    }

    private String findIndexByFeedId(String feedId) {
        if (feedId.contains("eng")) {
            return feedId.substring(0, feedId.indexOf("-", feedId.indexOf("eng"))) + "*";
        } else if (feedId.contains("kor")) {
            return feedId.substring(0, feedId.indexOf("-", feedId.indexOf("kor"))) + "*";
        }
        return feedId.substring(0, feedId.indexOf("-", 12)) + "*";
    }

    @Override
    public FeedModel getFeed(String docId, String connectomeId) {
        SearchHits searchHits = searchRepository.getFeed(docId, connectomeId);

        Iterator<SearchHit> iter = searchHits.iterator();

        while (iter.hasNext()) {
            SearchHit hit = iter.next();
            try {
                return FeedModel.convertToFeedDocument(hit);
//                FeedContentModel feedContentModel = FeedContentModel.convertToFeedDocument(searchRepository.getFeedContent(feedModel.getDocId_content()).iterator().next());
//                return new FeedDataModel(feedModel, feedContentModel);
            } catch (Exception e) {
                logger.error("Error content in search feed, " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    @Override
    public List<DocContentModel> getListFeedContent(List<String> feedContentId) {

        List<DocContentModel> docContentModels = new ArrayList<>();

//        List<String> contentIds = convertToListFeedContent(feedContentId);
        SearchHits searchHits = searchRepository.getListFeedContent(feedContentId);

        Iterator<SearchHit> iter = searchHits.iterator();

        while (iter.hasNext()) {
            SearchHit hit = iter.next();
            try {
                docContentModels.add(DocContentModel.convertToFeedDocument(hit));
            } catch (Exception e) {
                logger.error("Error content in get List feed content, " + e.getMessage());
                return null;
            }
        }
        return docContentModels;
    }

    @Override
    public ResponseListData<?> getListDocumentByIds(List<String> docIds, String connectomeId, Integer page, Integer size) {

        ResponseListData<FeedModel> responseListData = new ResponseListData<FeedModel>();

        Set<String> docIdSet = Sets.newHashSet(docIds);

        SearchHits searchHits = searchRepository.getListDocumentByIds(docIdSet, connectomeId, page, size);

        Iterator<SearchHit> iter = searchHits.iterator();
        List<FeedModel> feedDocuments = new ArrayList<>();

        while (iter.hasNext()) {
            SearchHit hit = iter.next();
            try {
                FeedModel feedModel = FeedModel.convertToFeedDocument(hit);
                if (feedModel != null) {
                    feedDocuments.add(feedModel);
                } else {
                    //Todo nothing
                }

            } catch (Exception e) {
                logger.error("Error content in get list feed, " + e.getMessage());
            }

        }
        responseListData.setTotalPages(getTotalPage(searchHits.getTotalHits().value, size));
        responseListData.setTotalItems(searchHits.getTotalHits().value);
        responseListData.setCurrentPage(page);
        responseListData.setData(feedDocuments);
        return responseListData;
    }

    @Override
    public ResponseListData<FeedModel> getListFilterFeed(String connectomeId, Integer page, Integer size, String type) {
        ResponseListData<FeedModel> responseListData = new ResponseListData<>();

        SearchHits searchHits = searchRepository.getListFilterFeed(connectomeId, page, size, type);

        Iterator<SearchHit> iter = searchHits.iterator();
        List<FeedModel> feedDocuments = new ArrayList<>();

        while (iter.hasNext()) {
            SearchHit hit = iter.next();
            try {
                FeedModel feedModel = FeedModel.convertToFeedDocument(hit);

                if (feedModel != null) {
                    feedDocuments.add(feedModel);
                } else {
                    //Todo nothing
                }

            } catch (Exception e) {
                logger.error("Error content in get list filter feed, " + e.getMessage());
            }

        }
        responseListData.setTotalPages(getTotalPage(searchHits.getTotalHits().value, size));
        responseListData.setTotalItems(searchHits.getTotalHits().value);
        responseListData.setCurrentPage(page);
        responseListData.setData(feedDocuments);
        return responseListData;
    }

    @Override
    public ResponseDocument<?> getDocumentById(RequestBodyGetDocument requestBody) {
        Callable<DocContentModel> docContentModelCallable = new Callable<DocContentModel>() {
            @Override
            public DocContentModel call() throws Exception {
                return getFeedContent(requestBody.getDocId());
            }
        };
        Future<DocContentModel> docContentModelFuture = executorService.submit(docContentModelCallable);

        ResponseDocument<DocumentModel> responseDocument = new ResponseDocument<DocumentModel>();

        if (!Objects.equals(requestBody.getConnectomeId(), "")) {
            SearchHits searchFeedHits = searchRepository.getFeed(requestBody.getDocId(), requestBody.getConnectomeId());

            Iterator<SearchHit> feedIter = searchFeedHits.iterator();

            while (feedIter.hasNext()) {
                SearchHit hit = feedIter.next();
                try {
                    DocumentModel feedModel = DocumentModel.convertToFeedDocument(hit);

                    if (feedModel != null && !requestBody.getRequire_content()) {
                        responseDocument.setData(feedModel);
                        responseDocument.setResult("success");
                        responseDocument.setResult_code(0);
                        responseDocument.setRequestId(requestBody.getRequestId());
                        return responseDocument;
                    } else if (feedModel != null && requestBody.getRequire_content()) {
                        ResponseDocument<DocumentDataModel> responseDocumentData = new ResponseDocument<DocumentDataModel>();

                        DocContentModel docContentModel = docContentModelFuture.get();
                        DocumentDataModel documentDataModel = new DocumentDataModel(feedModel, docContentModel);
                        responseDocumentData.setData(documentDataModel);

                        responseDocumentData.setResult("success");
                        responseDocumentData.setResult_code(0);
                        responseDocumentData.setRequestId(requestBody.getRequestId());

                        return responseDocumentData;
                    }

                } catch (Exception e) {
                    logger.error(String.format("[requestId: %s], Error content in get Document by Id, %s", requestBody.getRequestId(), e.getMessage()));
                }
            }
        } else {
            try {
                ResponseDocument<DocContentModel> modelResponseDocument = new ResponseDocument<>();
                modelResponseDocument.setResult("success");
                modelResponseDocument.setResult_code(0);
                modelResponseDocument.setRequestId(requestBody.getRequestId());
                modelResponseDocument.setData(docContentModelFuture.get());
                return modelResponseDocument;
            } catch (Exception e){
                logger.info(String.format("connectomeId: %s, docId: %s can not get Document content from ES , message: %s", requestBody.getConnectomeId(), requestBody.getDocId(), e.getMessage()));
            }
        }

        return responseDocument.getFailResponse(requestBody);
    }

    @Override
    public ResponseListData<?> getListDocumentByIds(RequestBodyGetListDoc requestBody) {

        Set<String> docIdSet = Sets.newHashSet(requestBody.getDocIds());

        ResponseListData<?> responseFailed = new ResponseListData<>().getFailResponse(requestBody);
        Callable<ResponseListData<DocumentModel>> docModelListCall = new Callable<ResponseListData<DocumentModel>>() {
            @Override
            public ResponseListData<DocumentModel> call() throws Exception {
                ResponseListData<DocumentModel> response = new ResponseListData<>();

                SearchHits searchHits = searchRepository.getListDocumentByIds(docIdSet, requestBody.getConnectomeId(), requestBody.getPage(), requestBody.getSize());
                Iterator<SearchHit> iter = searchHits.iterator();
                List<DocumentModel> feedDocuments = new ArrayList<>();

                while (iter.hasNext()) {
                    SearchHit hit = iter.next();
                    try {
                        DocumentModel feedModel = DocumentModel.convertToFeedDocument(hit);
                        if (feedModel != null) {
                            feedDocuments.add(feedModel);
                        } else {
                            //todo nothing
                        }
                    } catch (Exception e) {
                        logger.error("Error content in get list Document, " + e.getMessage());
                    }
                }
                response.setResult("success");
                response.setResult_code(0);
                response.setTotalPages(getTotalPage(searchHits.getTotalHits().value, requestBody.getSize()));
                response.setTotalItems(searchHits.getTotalHits().value);
                response.setCurrentPage(requestBody.getPage());
                response.setData(feedDocuments);
                return response;
            }
        };

        if (!Objects.equals(requestBody.getConnectomeId(), "")) {
            if (!requestBody.getRequire_content()) {
                try {
                    return executorService.submit(docModelListCall).get(5, TimeUnit.SECONDS);
                } catch (Exception e){
                    logger.error(String.format("connectomeId: %s, docIds: %s, get List Doc From ES error: %s", requestBody.getConnectomeId(), requestBody.getDocIds(), e.getMessage()));
                    return responseFailed;
                }
            } else if(requestBody.getSize() == null || requestBody.getPage() == null || requestBody.getSize() <= 2 * requestBody.getDocIds().size()){
                try {
                    Future<ResponseListData<DocumentModel>> futureResponse = executorService.submit(docModelListCall);

                    Map<String, DocContentModel> contentModelMap = new HashMap<>();

                    SearchHits contentHits = searchRepository.getListFeedContent(requestBody.getDocIds());

                    Iterator<SearchHit> docContentIter = contentHits.iterator();

                    while (docContentIter.hasNext()) {
                        SearchHit contentHit = docContentIter.next();
                        try {
                            DocContentModel docContentModel = DocContentModel.convertToFeedDocument(contentHit);
                            if (docContentModel != null) {
                                try {
                                    if (contentModelMap.get(docContentModel.getDocId()) == null || docContentModel.getContent() != null) {
                                        contentModelMap.put(docContentModel.getDocId(), docContentModel);
                                    }
                                } catch (NullPointerException e) {
                                    logger.error(String.format("[DocId: %s] content null", docContentModel));
                                }
                            }
                        } catch (Exception e) {
                            logger.error("Error content in get list document content, " + e.getMessage());
                        }
                    }

                    ResponseListData<DocumentModel> tempResponse = futureResponse.get();

                    ResponseListData<DocumentDataModel> responseListDocData = new ResponseListData<DocumentDataModel>();
                    List<DocumentDataModel> documentDataModels = new ArrayList<>();

                    for (DocumentModel feedModel : tempResponse.getData()) {
                        documentDataModels.add(new DocumentDataModel(feedModel, contentModelMap.get(feedModel.getDocId())));
                    }

                    responseListDocData.setResult(tempResponse.getResult());
                    responseListDocData.setResult_code(tempResponse.getResult_code());
                    responseListDocData.setTotalPages(tempResponse.getTotalPages());
                    responseListDocData.setTotalItems(tempResponse.getTotalItems());
                    responseListDocData.setCurrentPage(tempResponse.getCurrentPage());

                    responseListDocData.setData(documentDataModels);
                    return responseListDocData;
                } catch (Exception e){
                    logger.error(String.format("connectomeId: %s, docIds: %s, get List Doc From ES error: %s", requestBody.getConnectomeId(), requestBody.getDocIds(), e.getMessage()));
                    return responseFailed;
                }
            } else {
                try {
                    ResponseListData<DocumentModel> tempResponse = executorService.submit(docModelListCall).get(5,TimeUnit.SECONDS);

                    List<String> docIds = new LinkedList<>();

                    for (DocumentModel documentModel : tempResponse.getData()){
                        docIds.add(documentModel.getDocId());
                    }
                    Map<String, DocContentModel> contentModelMap = new HashMap<>();

                    SearchHits contentHits = searchRepository.getListFeedContent(docIds);

                    Iterator<SearchHit> docContentIter = contentHits.iterator();

                    while (docContentIter.hasNext()) {
                        SearchHit contentHit = docContentIter.next();
                        try {
                            DocContentModel docContentModel = DocContentModel.convertToFeedDocument(contentHit);
                            if (docContentModel != null) {
                                try {
                                    if (contentModelMap.get(docContentModel.getDocId()) == null || docContentModel.getContent() != null) {
                                        contentModelMap.put(docContentModel.getDocId(), docContentModel);
                                    }
                                } catch (NullPointerException e) {
                                    logger.error(String.format("[DocId: %s] content null", docContentModel));
                                }
                            }
                        } catch (Exception e) {
                            logger.error("Error content in get list document content, " + e.getMessage());
                        }
                    }

                    ResponseListData<DocumentDataModel> responseListDocData = new ResponseListData<DocumentDataModel>();
                    List<DocumentDataModel> documentDataModels = new ArrayList<>();

                    for (DocumentModel feedModel : tempResponse.getData()) {
                        documentDataModels.add(new DocumentDataModel(feedModel, contentModelMap.get(feedModel.getDocId())));
                    }

                    responseListDocData.setResult(tempResponse.getResult());
                    responseListDocData.setResult_code(tempResponse.getResult_code());
                    responseListDocData.setTotalPages(tempResponse.getTotalPages());
                    responseListDocData.setTotalItems(tempResponse.getTotalItems());
                    responseListDocData.setCurrentPage(tempResponse.getCurrentPage());
                    responseListDocData.setData(documentDataModels);
                    return responseListDocData;

                } catch (Exception e) {
                    logger.error(String.format("connectomeId: %s, docIds: %s, get List Doc From ES error: %s", requestBody.getConnectomeId(), requestBody.getDocIds(), e.getMessage()));
                    return responseFailed;
                }
            }
        } else {
            ResponseListData<DocContentModel> responseListData = new ResponseListData<>();
            List<DocContentModel> docContentModels = getListFeedContent(requestBody.getDocIds());
            responseListData.setData(docContentModels);
            responseListData.setResult("success");
            responseListData.setResult_code(0);
            responseListData.setCurrentPage(requestBody.getPage());
            responseListData.setTotalItems(Long.valueOf(docContentModels.size()));
            responseListData.setRequestId(requestBody.getRequestId());
            responseListData.setTotalPages(getTotalPage(docContentModels.size(), requestBody.getSize()));
            return responseListData;
        }
    }

    private Integer getTotalPage(long totalItems, Integer size) {
        try {
            if (totalItems % size == 0) {
                return Math.toIntExact(totalItems / size);
            }
            return Math.toIntExact(totalItems / size + 1);
        } catch (NullPointerException e){
            logger.info(String.format("totalItem: %s, size: %s can not get totalPage, message; %s", totalItems, size, e.getMessage()));
            return null;
        }
    }

    private List<String> convertToListFeedContent(String feedContentId) {
        List<String> contentIdList = new ArrayList<>();
        String[] contentIds = feedContentId.split(",");
        for (int i = 0; i < contentIds.length; i++) {
            contentIdList.add(contentIds[i].trim());
        }
        return contentIdList;
    }

    private List<String> convertDate(String searchDate) {
        if (searchDate == null || searchDate.isEmpty() || searchDate.length() < 2) return null;
        String type = searchDate.substring(searchDate.length() - 1);
        Integer number = Integer.parseInt(searchDate.substring(0, searchDate.length() - 1));
        try {
            List<String> dates = DateUtil.add(number, type);
            return dates;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Date convertStringToDate(String date) {
        try {
            if (date != null) {
                return simpleDateFormat.parse(date);
            }
            return null;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
