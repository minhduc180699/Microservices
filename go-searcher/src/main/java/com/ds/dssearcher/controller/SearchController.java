package com.ds.dssearcher.controller;

import com.ds.dssearcher.dto.AgentTwitterSearchDto;
import com.ds.dssearcher.dto.TwitterDto;
import com.ds.dssearcher.entity.*;
import com.ds.dssearcher.model.*;
import com.ds.dssearcher.response.ResponseDocument;
import com.ds.dssearcher.response.ResponseListData;
import com.ds.dssearcher.response.ResponseObject;
import com.ds.dssearcher.service.IEntityLinkingService;
import com.ds.dssearcher.service.ISearchSevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class SearchController {
    public static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api-service.host}")
    private String host;

    @Value("${api-service.tornado-metadata-access.name}")
    private String hostNameTornadoManager;

    @Value("${api-service.getAgentInfo}")
    private String apiGetAgentInfo;

    @Value("${api-service.realtime-social-crawler.name}")
    private String hostNameRealTimeCrawler;

    @Value("${api-service.searchTweetByScreenName}")
    private String apisearchTweetByScreenName;

    @Value("${api-service.updateAgentTwitterError}")
    private String apiUpdateAgentTwitterError;

    @Value("${api-service.searchTweetByKeyword}")
    private String apisearchTweetByKeyword;

    @Value("${api-service.getAgentTwitterSearch}")
    private String apiGetAgentTwitterSearch;

    @Autowired
    private ISearchSevice iSearchSevice;

    @Autowired
    private IEntityLinkingService iEntityLinkingService;

    @GetMapping("/api/doc/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            DetailDocumentEntity detailDocumentEntity = iSearchSevice.getById(id);
            if (detailDocumentEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(detailDocumentEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error has occurred");
        }
    }

    /**
     * Module: Ds-web
     */
    @GetMapping("/api/search")
    public ResponseEntity<?> searchKeyword(@RequestParam(value = "keyword", required = false) String keyword,
                                           @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                                           @RequestParam(value = "lang", required = false) String lang,
                                           @RequestParam(value = "searchType", required = false) String searchType,
                                           @RequestParam(value = "searchRange", required = false) String searchRange,
                                           @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                           @RequestParam(value = "authors", required = false) List<String> authors,
                                           @RequestParam(value = "channels", required = false) String channels,
                                           @RequestParam(value = "sortBy", required = false, defaultValue = "basic") String sortBy,
                                           @RequestParam(value = "ReverseSort", required = false, defaultValue = "false") String reverseSort,
                                           @RequestParam(value = "from", required = false) String from,
                                           @RequestParam(value = "util", required = false) String util,
                                           @RequestParam(value = "score", required = false) Integer score,
                                           @RequestParam(value = "entityLinking_type", defaultValue = "none", required = false) String entityLinkingType) {

        SearchResult searchResult = null;
        try {
            searchResult = iSearchSevice.searchKeyword(keyword, page, searchType, lang, searchRange, size, authors, channels, sortBy, reverseSort, score, from, util, entityLinkingType);
            if (entityLinkingType.equalsIgnoreCase("full")) {
                ViewSearchResult.SearchResultLinking searchResultLinking = new ViewSearchResult.SearchResultLinking();
                return ResponseEntity.status(HttpStatus.OK).body(searchResultLinking.covert(searchResult));
            } else if (entityLinkingType.equalsIgnoreCase("summary")) {
                ViewSearchResult.SearchResultSummary searchResultLinking = new ViewSearchResult.SearchResultSummary();
                return ResponseEntity.status(HttpStatus.OK).body(searchResultLinking.covert(searchResult));
            } else if (entityLinkingType.equalsIgnoreCase("all")) {
                return ResponseEntity.status(HttpStatus.OK).body(searchResult);
            } else {
                ViewSearchResult.SearchResultNone searchResultLinking = new ViewSearchResult.SearchResultNone();
                return ResponseEntity.status(HttpStatus.OK).body(searchResultLinking.covert(searchResult));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK).body(new SearchResult("faild", 999, new ArrayList<>()));
        }

    }

    @GetMapping("/api/searchContent")
    public ResponseEntity<?> searchContent(@RequestParam(value = "keyword", required = false) String keyword,
                                           @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                                           @RequestParam(value = "lang", required = false) String lang,
                                           @RequestParam(value = "searchType", required = false) String searchType,
                                           @RequestParam(value = "searchRange", required = false) String searchRange,
                                           @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                           @RequestParam(value = "authors", required = false) List<String> authors,
                                           @RequestParam(value = "channels", required = false) String channels,
                                           @RequestParam(value = "sortBy", required = false, defaultValue = "basic") String sortBy,
                                           @RequestParam(value = "ReverseSort", required = false, defaultValue = "false") String reverseSort,
                                           @RequestParam(value = "from", required = false) String from,
                                           @RequestParam(value = "util", required = false) String util,
                                           @RequestParam(value = "score", required = false) Integer score) {

        SearchResult searchResult = null;
        try {
            logger.info("search keyword start thread {}", Thread.currentThread().getId());
            searchResult = iSearchSevice.searchContent(keyword, page, searchType, lang, searchRange, size, authors, channels, sortBy, reverseSort, score, from, util);
            logger.info("search keyword end thread {}", Thread.currentThread().getId());
            return ResponseEntity.status(HttpStatus.OK).body(searchResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK).body(new SearchResult("faild", 999, new ArrayList<>()));
        }

    }

    /**
     * Module: Ds-web
     */
    @GetMapping("/api/search-people")
    public ResponseEntity<?> searchPeople(@RequestParam(value = "authorIds", required = false) List<String> authorIds,
                                          @RequestParam(value = "authors", required = false) List<String> authors,
                                          @RequestParam(value = "lang", required = false) String language,
                                          @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                                          @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                          @RequestParam(value = "channels", defaultValue = "twitter,facebook,instagram", required = false) String channels) {


        SearchResultPeople searchResult = null;
        try {
            logger.info("start search people thread {}", Thread.currentThread().getId());
            searchResult = iSearchSevice.searchPeople(channels, authors, authorIds, page, size, language);
            logger.info("end search people thread {}", Thread.currentThread().getId());
            return ResponseEntity.status(HttpStatus.OK).body(searchResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK).body(new SearchResult("faild", 999, new ArrayList<>()));
        }


    }

    /**
     * Module: Personal Agent Manager
     */
    @GetMapping("/deepsignal/personalAgentManagerApi/famous/getDataOfDatasources")
    public ResponseEntity<?> searchPeople(@RequestParam(value = "data_source_ids") String datSourceIds,
                                          @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                                          @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                          @RequestParam(value = "channels", required = false) String channels,
                                          @RequestParam(value = "serviceType", required = false) String serviceType,
                                          @RequestParam(value = "lang", required = false) String lang) {


        if (datSourceIds == null || datSourceIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new SearchResult("faild", 101, new ArrayList<>()));
        }
        SearchResultDataSource searchResult = null;
        try {
            searchResult = iSearchSevice.searchSourceData(datSourceIds, channels, page, size, serviceType, lang);
            return ResponseEntity.status(HttpStatus.OK).body(searchResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK).body(new SearchResult("faild", 999, new ArrayList<>()));
        }


    }

    /**
     * Module: Personal Agent Manager
     */
    @GetMapping("/deepsignal/personalAgentManagerApi/famous/countDataOfDatasources")
    public ResponseEntity<?> countDataSourceById(@RequestParam(value = "data_source_ids") String datSourceIds,
                                                 @RequestParam(value = "channels", required = false) String channels,
                                                 @RequestParam(value = "serviceType", required = false) String serviceType,
                                                 @RequestParam(value = "lang", required = false) String lang) {


        if (datSourceIds == null || datSourceIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new SearchResult("faild", 101, new ArrayList<>()));
        }
        ResultCountDatasource searchResult = null;
        try {
            searchResult = iSearchSevice.countDataSourceById(datSourceIds, channels, serviceType, lang);
            return ResponseEntity.status(HttpStatus.OK).body(searchResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK).body(new SearchResult("faild", 999, new ArrayList<>()));
        }

    }

    /**
     * Module: RSC
     */
    @GetMapping("/api/real-time/search-people")
    public ResponseEntity<?> searchRealTimePeople(@RequestParam(value = "accountId") String accountId,
                                                  @RequestParam(value = "refreshTime", required = false, defaultValue = "5m") String refreshTime,
                                                  @RequestParam(value = "lang", required = false, defaultValue = "en") String lang,
                                                  @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                                  @RequestParam(value = "channel", required = false) String channel,
                                                  @RequestParam(value = "requestId", required = false) Integer requestId) {


        SearchResultDataRealtimePeople searchResult = null;
        DocumentRealtimePeopleSearchEntity documentRealtimePeopleSearchEntity = new DocumentRealtimePeopleSearchEntity();
        String url = null;
        UriComponentsBuilder builder = null;
        if (accountId.contains("http://")) {
            String[] arr = accountId.split("/");
            accountId = arr[arr.length - 1];
        }
        url = host + hostNameTornadoManager + apiGetAgentInfo;
        builder = UriComponentsBuilder.fromHttpUrl(url);
        builder.queryParam("accountId", accountId);
        builder.queryParam("refreshTime", refreshTime);
        builder.queryParam("lang", lang);
        ResponseEntity<TwitterDto> response = null;
        try {
            response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, TwitterDto.class);
            if (response.getBody() != null) {
                logger.info("search tornado not null");
                ResponseEntity<ResponeRealtimeCrawler> result = null;
                try {
                    url = host + hostNameRealTimeCrawler + apisearchTweetByScreenName;
                    builder = UriComponentsBuilder.fromHttpUrl(url);
                    response.getBody().setTweetSize(size);
                    response.getBody().setRequestId(requestId);
                    HttpEntity<TwitterDto> entity = new HttpEntity<>(response.getBody());
                    result = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, ResponeRealtimeCrawler.class);
                    if (result.getBody().getResult_code() == 0) {
                        logger.info("search realtim crawler success");
                        searchResult = new SearchResultDataRealtimePeople();
                        searchResult.setRequestId(requestId);
                        searchResult.setData(documentRealtimePeopleSearchEntity.covnertData(result.getBody().getData(), response.getBody().getAgentType()));
                        searchResult.setResult_code(result.getBody().getResult_code());
                        searchResult.setResult(result.getBody().getResult());
                    } else if (result.getBody().getResult_code() == -1) {
                        logger.info("search realtim crawler code -1");
                        searchResult = searchInternal(channel, accountId, size, lang, requestId, documentRealtimePeopleSearchEntity);
                        try {
                            url = host + hostNameTornadoManager + apiUpdateAgentTwitterError;
                            builder = UriComponentsBuilder.fromHttpUrl(url);
                            builder.queryParam("agentId", response.getBody().getAgentid());
                            restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, null, Boolean.class);
                            logger.info("update agent success");
                        } catch (Exception e) {
                            logger.error("update agent error");
                        }
                    }
                } catch (Exception e) {
                    logger.error("search realtim crawler error");
                    searchResult = searchInternal(channel, accountId, size, lang, requestId, documentRealtimePeopleSearchEntity);
                    try {
                        url = host + hostNameTornadoManager + apiUpdateAgentTwitterError;
                        builder = UriComponentsBuilder.fromHttpUrl(url);
                        builder.queryParam("agentId", response.getBody().getAgentid());
                        restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, null, Boolean.class);
                        logger.info("update agent success");
                    } catch (Exception ex) {
                        logger.error("update agent error");
                    }
                }
            } else {
                logger.info("search tornado null");
                searchResult = searchInternal(channel, accountId, size, lang, requestId, documentRealtimePeopleSearchEntity);
            }

        } catch (Exception e) {
            logger.error("get api tornado manger error");
            searchResult = searchInternal(channel, accountId, size, lang, requestId, documentRealtimePeopleSearchEntity);
        }
        if (searchResult != null) {
            return ResponseEntity.status(HttpStatus.OK).body(searchResult);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new SearchResult("faild", 999, new ArrayList<>()));
        }

    }

    private SearchResultDataRealtimePeople searchInternal(String channel, String accountId, Integer size, String lang, Integer requestId,
                                                          DocumentRealtimePeopleSearchEntity documentRealtimePeopleSearchEntity) {
        SearchResultDataRealtimePeople searchResult = new SearchResultDataRealtimePeople();
        try {
            SearchResultPeople searchResultPeople = iSearchSevice.searchPeople(channel, Arrays.asList(accountId), null, 1, size, lang);
            searchResult.setData(documentRealtimePeopleSearchEntity.covert(searchResultPeople.getData(), accountId));
            searchResult.setResult_code(searchResultPeople.getResult_code());
            searchResult.setResult(searchResultPeople.getResult());
            searchResult.setRequestId(requestId);
        } catch (Exception e) {
            logger.error("search internal error");
            return null;
        }
        return searchResult;
    }

    /**
     * Module: RSC
     */
    @GetMapping("/api/real-time/search-keyword")
    public ResponseEntity<?> searchRealTimeSocialKeyword(@RequestParam(value = "keyword") String keyword,
                                                         @RequestParam(value = "refreshTime", required = false, defaultValue = "5m") String refreshTime,
                                                         @RequestParam(value = "lang", required = false, defaultValue = "en") String lang,
                                                         @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                                         @RequestParam(value = "channel", required = false) String channel
    ) throws UnsupportedEncodingException {


        SearchResultDataRealtimeKeyword searchResult = null;
        DocumentRealtimeKeywordSearchEntity documentRealtimeKeywordSearchEntity = new DocumentRealtimeKeywordSearchEntity();
        String url = null;
        UriComponentsBuilder builder = null;
        url = host + hostNameTornadoManager + apiGetAgentTwitterSearch;
        builder = UriComponentsBuilder.fromHttpUrl(url);
        builder.queryParam("keyword", keyword);
        builder.queryParam("refreshTime", refreshTime);
        builder.queryParam("lang", lang);
        ResponseEntity<AgentTwitterSearchDto> response = null;
        try {
            response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, AgentTwitterSearchDto.class);
            if (response.getBody() != null) {
                logger.info("search tornado twitter search not null");
                ResponseEntity<ResponseRealtimeSearch> result = null;
                try {
                    url = host + hostNameRealTimeCrawler + apisearchTweetByKeyword;
                    builder = UriComponentsBuilder.fromHttpUrl(url);
                    response.getBody().setTweetSize(size);
                    HttpEntity<AgentTwitterSearchDto> entity = new HttpEntity<>(response.getBody());
                    result = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, ResponseRealtimeSearch.class);
                    if (result.getBody().getResult_code() == 0) {
                        logger.info("search realtim search keyword success");
                        searchResult = new SearchResultDataRealtimeKeyword();
                        searchResult.setData(documentRealtimeKeywordSearchEntity.covnertData(result.getBody().getData(), 5, keyword));
                        searchResult.setResult_code(result.getBody().getResult_code());
                        searchResult.setResult(result.getBody().getResult());
                    } else if (result.getBody().getResult_code() == -1) {
                        logger.info("search realtime search keyword code -1");
                        searchResult = searchKeyword(channel, keyword, size, lang, documentRealtimeKeywordSearchEntity);
                        try {
                            url = host + hostNameTornadoManager + apiUpdateAgentTwitterError;
                            builder = UriComponentsBuilder.fromHttpUrl(url);
                            builder.queryParam("agentId", response.getBody().getAgentid());
                            restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, null, Boolean.class);
                            logger.info("update agent success");
                        } catch (Exception e) {
                            logger.error("update agent error");
                        }
                    }
                } catch (Exception e) {
                    logger.error("search realtime search keyword error");
                    searchResult = searchKeyword(channel, keyword, size, lang, documentRealtimeKeywordSearchEntity);
                    try {
                        url = host + hostNameTornadoManager + apiUpdateAgentTwitterError;
                        builder = UriComponentsBuilder.fromHttpUrl(url);
                        builder.queryParam("agentId", response.getBody().getAgentid());
                        restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, null, Boolean.class);
                        logger.info("update agent success");
                    } catch (Exception ex) {
                        logger.error("update agent error");
                    }
                }
            } else {
                logger.info("search tornado twitter search null");
                searchResult = searchKeyword(channel, keyword, size, lang, documentRealtimeKeywordSearchEntity);
            }

        } catch (Exception e) {
            logger.error("get api tornado manger twitter search error");
            searchResult = searchKeyword(channel, keyword, size, lang, documentRealtimeKeywordSearchEntity);
        }
        if (searchResult != null) {
            return ResponseEntity.status(HttpStatus.OK).body(searchResult);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new SearchResult("faild", 999, new ArrayList<>()));
        }

    }

    private SearchResultDataRealtimeKeyword searchKeyword(String channel, String keyword, Integer size, String lang, DocumentRealtimeKeywordSearchEntity documentRealtimeKeywordSearchEntity) {
        SearchResultDataRealtimeKeyword searchResult = new SearchResultDataRealtimeKeyword();
        try {
            SearchResultPeople searchResultPeople = iSearchSevice.searchKeyword(channel, keyword, 1, size, lang);
            searchResult.setData(documentRealtimeKeywordSearchEntity.covert(searchResultPeople.getData(), keyword));
            searchResult.setResult_code(searchResultPeople.getResult_code());
            searchResult.setResult(searchResultPeople.getResult());
        } catch (Exception e) {
            logger.error("search keyword error");
            return null;
        }
        return searchResult;
    }

    /**
     * Module: entityLinking
     */
    @GetMapping("/api/deepsignal/entityLinkingSearch")
    public ResponseEntity<?> entityLinkingSearch(@RequestParam(value = "request_id", required = false) String requestId,
                                                 @RequestParam(value = "doc_id", required = false) String docId,
                                                 @RequestParam(value = "entityLinking_type", defaultValue = "all", required = false) String entityLinkingType) {


        if (docId == null || docId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseEntityLinking("NO_DOCID", 101, requestId, docId, null, null));
        }

        ResponseEntityLinking responseEntityLinking = null;
        try {
            responseEntityLinking = iEntityLinkingService.entityLinkingSearch(requestId, docId, entityLinkingType);
            if (entityLinkingType.equalsIgnoreCase("full")) {
                ViewEntityLinking.ResponseEntityLinking entityLinking = new ViewEntityLinking.ResponseEntityLinking(responseEntityLinking.getResult(), responseEntityLinking.getResult_code(),
                        responseEntityLinking.getRequest_id(), responseEntityLinking.getDoc_id(), responseEntityLinking.getEntityLinking());
                return ResponseEntity.status(HttpStatus.OK).body(entityLinking);
            } else if (entityLinkingType.equalsIgnoreCase(("summary"))) {
                ViewEntityLinking.ResponseEntityLinkingSummary entityLinking = new ViewEntityLinking.ResponseEntityLinkingSummary(responseEntityLinking.getResult(), responseEntityLinking.getResult_code(),
                        responseEntityLinking.getRequest_id(), responseEntityLinking.getDoc_id(), responseEntityLinking.getEntityLinking_summary());

                return ResponseEntity.status(HttpStatus.OK).body(entityLinking);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(responseEntityLinking);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseEntityLinking("UNKNOWN_ERROR", 999, requestId, docId, null, null));
        }


    }

//    @GetMapping("/goSearcher/getListDocument")
//    public ResponseEntity<?> getListFeed(@RequestParam(value = "connectomeId") String connectomeId,
//                                         @RequestParam(value = "page") Integer page,
//                                         @RequestParam(value = "size") Integer size) {
//        try {
//            return ResponseEntity.status(HttpStatus.OK).body(iSearchSevice.getListFeed(connectomeId, page, size));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.failResponse);
//        }
//    }

    @GetMapping("/goSearcher/getListFilterDocument")
    public ResponseEntity<?> getListFilterFeed(@RequestParam(value = "connectomeId") String connectomeId,
                                         @RequestParam(value = "page") Integer page,
                                         @RequestParam(value = "size") Integer size,
                                         @RequestParam(value = "type", required = false) String type) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(iSearchSevice.getListFilterFeed(connectomeId, page, size, type));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.failResponse);
        }
    }

    @GetMapping("/goSearcher/getDocument")
    public ResponseEntity<?> getFeed(@RequestParam(value = "docId", required = true) String docId,
                                     @RequestParam(value = "connectomeId", required = true) String connectomeId) {
        try {
            FeedModel feedModel = iSearchSevice.getFeed(docId, connectomeId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", 0, feedModel));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.failResponse);
        }
    }

    @GetMapping("/goSearcher/searchDocumentData")
    public ResponseEntity<?> searchFeed(
            @RequestParam(value = "connectomeId", required = true) String connectomeId,
            @RequestParam(value = "request_id", required = false) String requestId,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "until", required = false) String until,
            @RequestParam(value = "search_type", required = false) String searchType,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "channels", required = false) String channels,
            @RequestParam(value = "type", required = false, defaultValue = "PERSONAL_DOCUMENT") String type,
            @RequestParam(value = "lang", required = true) String lang,
            @RequestParam(value = "sortBy", required = false, defaultValue = "date") String sortBy,
            @RequestParam(value = "score", required = false) Float score,
            @RequestParam(value = "writer", required = false) List<String> writer
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(iSearchSevice.searchFeed(connectomeId, keyword, from, until, searchType, page, size, channels, lang, type, sortBy, score, writer));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.failResponse);
        }
    }

//    @GetMapping("/goSearcher/getListDocumentContent")
//    public ResponseEntity<?> getListFeedContent(@RequestBody(required = true) List<String> feedContentId) {
//        try {
//            List<FeedContentModel> feedContentModels = iSearchSevice.getListFeedContent(feedContentId);
//            ResponseObject responseObject = new ResponseObject("success", 0, feedContentModels);
//            return ResponseEntity.status(HttpStatus.OK).body(responseObject);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.failResponse);
//        }
//    }

    @GetMapping("/goSearcher/getDocumentContent")
    public ResponseEntity<?> getFeedContent(@RequestParam(value = "docId", required = true) String feedContentId) {
        try {
            DocContentModel docContentModel = iSearchSevice.getFeedContent(feedContentId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", 0, docContentModel));
        } catch (Exception e) {
            logger.error("can not get DocContent cause by: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.failResponse);
        }
    }

//    @PostMapping("/goSearcher/getListDocumentByIds")
//    public ResponseEntity<?> getListDocumentByIds(@RequestBody RequestBodyGetDocumentByIds requestBodyGetDocument) {
//        try {
//            return ResponseEntity.status(HttpStatus.OK).body(iSearchSevice.getListDocumentByIds(requestBodyGetDocument.getDocIds(), requestBodyGetDocument.getConnectomeId(), requestBodyGetDocument.getPage(), requestBodyGetDocument.getSize()));
//        } catch (Exception e) {
//            logger.error("can not get List document cause by: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.failResponse);
//        }
//    }


//    @ApiOperation(value = "api get document with content or not content",
//    response = ResponseDocument.class,
//    notes = "required docId and connectomeId")
    @PostMapping("/goSearcher/getDocumentById")
    public ResponseEntity<?> getDocumentById(@RequestBody RequestBodyGetDocument requestBody){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(iSearchSevice.getDocumentById(requestBody));
        } catch (Exception e) {
            logger.error("can not get Document cause by: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDocument<>().getFailResponse(requestBody));
        }
    }

//    @ApiOperation(value = "api get list document with content or not content",
//            response = RequestBodyGetListDoc.class,
//            notes = "required docIds and connectomeId")
    @PostMapping("/goSearcher/getListDocumentByIds")
    public ResponseEntity<?> getListDocumentByIds(@RequestBody RequestBodyGetListDoc requestBody) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(iSearchSevice.getListDocumentByIds(requestBody));
        } catch (Exception e) {
            logger.error("can not get List Document cause by: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseListData<>().getFailResponse(requestBody));
        }
    }

}
