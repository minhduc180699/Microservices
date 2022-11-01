package com.saltlux.deepsignal.feedcache.service;

import com.google.gson.JsonObject;
import com.saltlux.deepsignal.feedcache.api.client.RealtimeCrawlerClient;
import com.saltlux.deepsignal.feedcache.api.client.SearcherClient;
import com.saltlux.deepsignal.feedcache.constant.Constant;
import com.saltlux.deepsignal.feedcache.constant.KafkaConstant;
import com.saltlux.deepsignal.feedcache.dto.response.DataListResponse;
import com.saltlux.deepsignal.feedcache.dto.response.DataResponse;
import com.saltlux.deepsignal.feedcache.dto.response.ResponseDocument;
import com.saltlux.deepsignal.feedcache.dto.response.ResultResponse;
import com.saltlux.deepsignal.feedcache.kafka.Producer;
import com.saltlux.deepsignal.feedcache.model.*;
import com.saltlux.deepsignal.feedcache.model.request.RequestBodyGetDocument;
import com.saltlux.deepsignal.feedcache.model.request.RequestBodyGetListDoc;
import com.saltlux.deepsignal.feedcache.redis.RedisConnection;
import com.saltlux.deepsignal.feedcache.service.asyn.GetDocDataFromES;
import com.saltlux.deepsignal.feedcache.service.asyn.GetDocDataFromRedis;
import com.saltlux.deepsignal.feedcache.utils.GUtil;
import com.saltlux.deepsignal.feedcache.utils.Utils;
import io.lettuce.core.KeyValue;
import io.lettuce.core.RedisFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

@Service
public class FeedService implements IFeedService {
    private Logger logger = LoggerFactory.getLogger(FeedService.class);
    @Autowired
    private Producer producer;
    @Autowired
    private RedisConnection redisConnection;
    @Qualifier("com.saltlux.deepsignal.feedcache.api.client.SearcherClient")
    @Autowired
    private SearcherClient searcherClient;
    @Qualifier("com.saltlux.deepsignal.feedcache.api.client.RealtimeCrawlerClient")
    @Autowired
    private RealtimeCrawlerClient realtimeCrawlerClient;

    private static ExecutorService executorService = Executors.newFixedThreadPool(20);

    @Override
    public DataListResponse<DocModel> getListFeed(String connectomeId, String request_id, Integer page, Integer size) {
        DataListResponse<DocModel> response = new DataListResponse<>(0, "success", request_id);
        List<DocModel> docModelList = null;
        try {

            DataListResponse<DocModel> response1 = searcherClient.getListFeed(connectomeId, page, size);

            docModelList = response1.getData();
            if (docModelList != null && !docModelList.isEmpty()) {
                for (DocModel docModel : docModelList) {

                    // Save in Redis
                    redisConnection.saveValueToFeedAsSync(connectomeId + "_" + docModel.getDocId(), GUtil.gson.toJson(docModel));

                }
                logger.info(String.format("[getListFeedData] requestId: %s DONE success", request_id));
            }
            return response1;

        } catch (Exception e) {
            response.setStatus(-1, "failed");
            logger.error(String.format("[getListFeedData] requestId: %s error unknown" + e.getMessage(), request_id), e);
        }
        response.setData(docModelList);
        return response;
    }

    @Override
    public DataListResponse<DocModel> searchFeed(String connectomeId, String request_id, String keyword, String from, String until, Integer page, Integer size, String searchType, String channels, String lang, String type, String sortBy, Float score, List<String> writer) {
        DataListResponse<DocModel> response = new DataListResponse<>(0, "success", request_id);

        try {
            // Search fee data from Elasticsearch
            response = searcherClient.searchFeed(connectomeId, keyword, from, until, page, size, searchType, channels, lang, type, sortBy, score, writer);
            List<DocModel> docModelList = response.getData();
//            Map<String, DocModel> docModelHashMap = new HashMap<>();
//            List<String> keys = new ArrayList<>();
            for (DocModel docModel : docModelList) {
                redisConnection.saveValueToFeedAsSync(docModel.getConnectomeId() + "_" + docModel.getDocId(), GUtil.gson.toJson(docModel));
//                keys.add(docModel.getConnectomeId() + "_" + docModel.getDocId());
//                docModelHashMap.put(docModel.getConnectomeId() + "_" + docModel.getDocId(), docModel);
            }
            logger.info(String.format("[searchFeedData] requestId: %s DONE success", request_id));
        } catch (Exception e) {
            response.setStatus(-1, "failed");
            logger.error(String.format("[searchFeedData] requestId: %s error unknown" + e.getMessage(), request_id), e);
        }
        return response;
    }

    @Override
    public DataResponse<DocDataModel> getFeed(String connectomeId, String request_id, String docId) {
        DataResponse<DocDataModel> response = new DataResponse<>(0, "success", request_id);


        try {
//            // Get feed from redis
//            DocModel docModel = redisConnection.getValueOfFeed(connectomeId + "_" + docId);
//            DocContentModel docContentModel = null;
//            // Has feed
//            if (docModel != null) {
//                DocModel docModelFromSearcher = searcherClient.getFeed(connectomeId, docId).getData();
//                docModel = docModelFromSearcher.mergeDoc(docModel);
//                redisConnection.saveValueToFeedAsSync(docModel.getConnectomeId() + "_" + docModel.getDocId(), GUtil.gson.toJson(docModel));
//                // Check feed content in redis
//                docContentModel = redisConnection.getValueOfFeedContent(docId);
//                // Has not feed content: get feed content from Elasticsearch
//                if (docContentModel == null) {
//                    docContentModel = searcherClient.getFeedContent(docModel.getDocId()).getData();
//                    if (docContentModel != null) {
//                        redisConnection.saveValueToFeedContentAsSync(docContentModel.getDocId(), GUtil.gson.toJson(docContentModel));
//                    }
//                } else {
//                    DocContentModel docContentModelFromSearcher = searcherClient.getFeedContent(docId).getData();
//                    docContentModel = docContentModelFromSearcher.mergeDocContent(docContentModel);
//                    redisConnection.saveValueToFeedContentAsSync(docContentModel.getDocId(), GUtil.gson.toJson(docContentModel));
//                }
//            }
//            // Has not feed: Get feed & feed content from Elasticsearch
//            else {
//                docModel = searcherClient.getFeed(connectomeId, docId).getData();
//                docContentModel = searcherClient.getFeedContent(docId).getData();
//                // Execute task save feed into Redis
//                if (docModel != null) {
//                    redisConnection.saveValueToFeedAsSync(docModel.getConnectomeId() + "_" + docModel.getDocId(), GUtil.gson.toJson(docModel));
//                }
//                if (docContentModel != null) {
//                    redisConnection.saveValueToFeedContentAsSync(docContentModel.getDocId(), GUtil.gson.toJson(docContentModel));
//                }
//
//            }


//            feedDataResponse.buildFeedDataModel(docModel, docContentModel);
//            List<Callable<DocModel>> callables = new LinkedList<>();
//
//            GetDocFromRedis getDocFromRedis = new GetDocFromRedis(connectomeId + "_" + docId, redisConnection);
//
//            GetDocFromES getDocFromES = new GetDocFromES(connectomeId, docId,searcherClient);
//
//            callables.add(getDocFromRedis);
//            callables.add(getDocFromES);
//
//            List<Future<DocModel>> docModels = new LinkedList<>();
//
//            try {
//                docModels = executorService.invokeAll(callables);
//            }catch (InterruptedException e){
//                logger.error(String.format("api get Doc connectomeId: %s, docId: %s executor error : "+e.getMessage(), connectomeId, docId));
//                response.failResponse(request_id);
//                return response;
//            }
//
//            List<DocModel> docModelList = new LinkedList<>();
//
//            for (Future<DocModel> docModelFuture : docModels){
//                DocModel docModel = docModelFuture.get();
//                if(docModel != null){
//                    docModelList.add(docModelFuture.get());
//                }
//            }
//
//            DocModel docModel = new DocModel();
//            if(docModelList.size() > 1){
//                docModel = docModelList.get(0).mergeDoc(docModelList.get(1));
//            } else if(docModelList.size() < 1){
//                // todo nothing
//            } else {
//                docModel = docModelList.get(0);
//            }
//
//            DocContentModel docContentModel = searcherClient.getFeedContent(docId).getData();
//            feedDataResponse.buildFeedDataModel(docModel,docContentModel);

            GetDocDataFromES getDocDataFromES = new GetDocDataFromES(connectomeId, docId, searcherClient);
            Future<DocDataModel> docDataModelFromESFuture = executorService.submit(getDocDataFromES);

//            // save into redis to test
//            DocModel docModel = GUtil.gson.fromJson(GUtil.gson.toJson(docDataModelFromES), DocModel.class);
//            DocContentModel docContentModel = GUtil.gson.fromJson(GUtil.gson.toJson(docDataModelFromES), DocContentModel.class);
//            redisConnection.saveValueToFeedAsSync(connectomeId + "_" + docId, GUtil.gson.toJson(docModel));
//            redisConnection.saveValueToFeedContentAsSync(docId, GUtil.gson.toJson(docContentModel));

            GetDocDataFromRedis getDocDataFromRedis = new GetDocDataFromRedis(connectomeId, docId, redisConnection);
            DocDataModel docDataModelFromRedis = executorService.submit(getDocDataFromRedis).get(5, TimeUnit.SECONDS);

            DocDataModel docDataModel = docDataModelFromESFuture.get(5, TimeUnit.SECONDS);

            docDataModel.mergeDocData(docDataModelFromRedis);

            response.setData(docDataModel);

            logger.info(String.format("[getFeed] requestId: %s DONE success", request_id));
        } catch (Exception e) {
            response.setStatus(-1, "failed");
            logger.error(String.format("[getFeed] requestId: %s error unknown" + e.getMessage(), request_id), e);
        }
        return response;
    }

    @Override
    public DataListResponse<DocModel> getListFilterFeed(String connectomeId, String request_id, Integer page, Integer size, String type) {
        DataListResponse<DocModel> response = new DataListResponse<>(0, "success", request_id);
        List<DocModel> docModelList = null;
        try {

            DataListResponse<DocModel> response1 = searcherClient.getListFilterFeed(connectomeId, page, size, type);

            docModelList = response1.getData();
            if (docModelList != null && !docModelList.isEmpty()) {
                for (DocModel docModel : docModelList) {

                    // Save in Redis
                    redisConnection.saveValueToFeedAsSync(connectomeId + "_" + docModel.getDocId(), GUtil.gson.toJson(docModel));

                }
                logger.info(String.format("[getListFilterFeed] requestId: %s DONE success", request_id));
            }
            return response1;

        } catch (Exception e) {
            response.setStatus(-1, "failed");
            logger.error(String.format("[getListFilterFeed] requestId: %s error unknown" + e.getMessage(), request_id), e);
        }
        response.setData(docModelList);
        return response;
    }

    @Override
    public ResultResponse createFeed(DocCreateModel data) {
        ResultResponse response = new ResultResponse(0, "success", data.getRequestId());
        try {
            LinkedHashMap<String, Object> saveRecord = new LinkedHashMap<>();
            List<String> keyField = new ArrayList<>();
            String sourceId = null;

            if (data.getComment_id() != null && data.getReply_id() != null) {
                sourceId = data.getUrl() + "-" + data.getComment_id() + "-" + data.getReply_id();
            } else if (data.getComment_id() == null) {
                sourceId = data.getUrl() + "-0-" + data.getReply_id();
            } else if (data.getReply_id() == null) {
                sourceId = data.getUrl() + "-" + data.getComment_id() + "-0";
            } else {
                sourceId = data.getUrl() + "-0-0";
            }

            saveRecord.put("source_id", sourceId);
            keyField.add("source_id");
            Long uniqueKey = Utils.getHashCode(saveRecord, keyField);
            data.set__unique__key(uniqueKey);
            data.setCollector("RECOMMENDATION");
            producer.send(KafkaConstant.FEED_CREATE_TOPIC, GUtil.gson.toJson(data));
            producer.send(KafkaConstant.FEED_CREATE_TOPIC_TEST, GUtil.gson.toJson(data));
            try {
                if ((data.getContent() == null || data.getContent().isEmpty()) && !data.getUrl().contains("youtube")) {
                    realtimeCrawlerClient.postRealtimeCrawler(Collections.singletonList(data.toFeedRealtimeCrawlerModel()));
                }
            } catch (NullPointerException e) {
                realtimeCrawlerClient.postRealtimeCrawler(Collections.singletonList(data.toFeedRealtimeCrawlerModel()));
                // todo nothing
            }
            logger.info(String.format("[createFeed] requestId: %s DONE success", data.getRequestId()));
            logger.info(String.format("FeedModel pushed to Kafka: %s", GUtil.gson.toJson(data)));
        } catch (Exception e) {
            response.setStatus(-1, "failed");
            logger.error(String.format("[createFeed] requestId: %s error unknown" + e.getMessage(), data.getRequestId()), e);
        }
        return response;
    }

    @Override
    public ResultResponse updateFeed(FeedUpdateModel feedModel) {

        ResultResponse response = new ResultResponse(0, "success", feedModel.getRequestId());
        try {
            JsonObject data = GUtil.gson.toJsonTree(feedModel.getFeed()).getAsJsonObject();
            JsonObject feedJsonObject = redisConnection.getValueOfFeedTypeJsonObject(data.get(Constant.DocumentFieldName.CONNECTOME_ID_FIELD).getAsString() + "_" + data.get(Constant.DocumentFieldName.DOC_ID_CONTENT_FIELD).getAsString());

            if (feedJsonObject == null) {
                feedJsonObject = GUtil.gson.toJsonTree(searcherClient.getFeed(data.get(Constant.DocumentFieldName.CONNECTOME_ID_FIELD).getAsString(), data.get(Constant.DocumentFieldName.DOC_ID_CONTENT_FIELD).getAsString()).getData()).getAsJsonObject();
            }

            for (String key : data.keySet()) {
                feedJsonObject.remove(key);
                feedJsonObject.add(key, data.get(key));
            }
            redisConnection.saveValueToFeedAsSync(data.get(Constant.DocumentFieldName.CONNECTOME_ID_FIELD).getAsString() + "_" + data.get(Constant.DocumentFieldName.DOC_ID_CONTENT_FIELD).getAsString(), GUtil.gson.toJson(feedJsonObject));

            data.remove(Constant.DocumentFieldName.FEED_PARTITION_FIELD);
            data.add("feed_partition", feedJsonObject.get("feed_partition"));

            producer.send(KafkaConstant.FEED_UPDATE_TOPIC, GUtil.gson.toJson(data));
            logger.info(String.format("[updateFeed] requestId: %s DONE success", feedModel.getRequestId()));
        } catch (Exception e) {
            response.setStatus(-1, "failed");
            logger.error(String.format("[updateFeed] requestId: %s error unknown" + e.getMessage(), feedModel.getRequestId()), e);
        }
        return response;
    }

    @Override
    public ResultResponse likeFeed(FeedInputModel feedModel) {
        ResultResponse response = new ResultResponse(0, "success", feedModel.getRequestId());
        try {
            JsonObject data = GUtil.gson.toJsonTree(feedModel).getAsJsonObject();
            JsonObject feedJsonObject = redisConnection.getValueOfFeedTypeJsonObject(data.get(Constant.DocumentFieldName.CONNECTOME_ID_FIELD).getAsString() + "_" + data.get(Constant.DocumentFieldName.DOC_ID_CONTENT_FIELD).getAsString());
            if (feedJsonObject == null) {
                feedJsonObject = GUtil.gson.toJsonTree(searcherClient.getFeed(data.get(Constant.DocumentFieldName.CONNECTOME_ID_FIELD).getAsString(), data.get(Constant.DocumentFieldName.DOC_ID_CONTENT_FIELD).getAsString()).getData()).getAsJsonObject();
            }
            feedJsonObject.remove("liked");
            feedJsonObject.add("liked", data.get("liked"));

            redisConnection.saveValueToFeedAsSync(data.get(Constant.DocumentFieldName.CONNECTOME_ID_FIELD).getAsString() + "_" + data.get(Constant.DocumentFieldName.DOC_ID_CONTENT_FIELD).getAsString(), GUtil.gson.toJson(feedJsonObject));

            data.remove(Constant.DocumentFieldName.FEED_PARTITION_FIELD);
            data.add("feed_partition", feedJsonObject.get("feed_partition"));
            data.remove(Constant.DocumentFieldName.IS_DELETED_FIELD);
            data.remove(Constant.DocumentFieldName.IS_BOOKMARK_FIELD);
            producer.send(KafkaConstant.FEED_UPDATE_TOPIC, GUtil.gson.toJson(data));
            logger.error(String.format("[likeFeed] requestId: %s DONE success", feedModel.getRequestId()));
        } catch (Exception e) {
            response.setStatus(-1, "failed");
            logger.info(String.format("[likeFeed] requestId: %s error unknown" + e.getMessage(), feedModel.getRequestId()), e);
        }
        return response;
    }

    @Override
    public ResultResponse hideFeed(FeedInputModel feedModel) {
        ResultResponse response = new ResultResponse(0, "success", feedModel.getRequestId());
        try {
            JsonObject data = GUtil.gson.toJsonTree(feedModel).getAsJsonObject();
            JsonObject feedJsonObject = redisConnection.getValueOfFeedTypeJsonObject(data.get(Constant.DocumentFieldName.CONNECTOME_ID_FIELD).getAsString() + "_" + data.get(Constant.DocumentFieldName.DOC_ID_CONTENT_FIELD).getAsString());

            if (feedJsonObject == null) {
                feedJsonObject = GUtil.gson.toJsonTree(searcherClient.getFeed(data.get(Constant.DocumentFieldName.CONNECTOME_ID_FIELD).getAsString(), data.get(Constant.DocumentFieldName.DOC_ID_CONTENT_FIELD).getAsString()).getData()).getAsJsonObject();
            }

            feedJsonObject.remove("isDeleted");
            feedJsonObject.add("isDeleted", data.get("isDeleted"));
            redisConnection.saveValueToFeedAsSync(data.get(Constant.DocumentFieldName.CONNECTOME_ID_FIELD).getAsString() + "_" + data.get(Constant.DocumentFieldName.DOC_ID_CONTENT_FIELD).getAsString(), GUtil.gson.toJson(feedJsonObject));

            data.remove(Constant.DocumentFieldName.FEED_PARTITION_FIELD);
            data.add(Constant.DocumentFieldName.FEED_PARTITION_FIELD, feedJsonObject.get(Constant.DocumentFieldName.FEED_PARTITION_FIELD));

            data.remove(Constant.DocumentFieldName.LIKED_FIELD);
            data.remove(Constant.DocumentFieldName.IS_BOOKMARK_FIELD);
            producer.send(KafkaConstant.FEED_UPDATE_TOPIC, GUtil.gson.toJson(data));
            logger.info(String.format("[hideFeed] requestId: %s DONE success", feedModel.getRequestId()));
        } catch (Exception e) {
            response.setStatus(-1, "failed");
            logger.error(String.format("[hideFeed] requestId: %s error unknown" + e.getMessage(), feedModel.getRequestId()), e);
        }
        return response;
    }

    @Override
    public ResultResponse bookmarkFeed(FeedInputModel feedModel) {
        ResultResponse response = new ResultResponse(0, "success", feedModel.getRequestId());
        try {
            JsonObject data = GUtil.gson.toJsonTree(feedModel).getAsJsonObject();
            JsonObject feedJsonObject = redisConnection.getValueOfFeedTypeJsonObject(data.get(Constant.DocumentFieldName.CONNECTOME_ID_FIELD).getAsString() + "_" + data.get(Constant.DocumentFieldName.DOC_ID_CONTENT_FIELD).getAsString());

            if (feedJsonObject == null) {
                feedJsonObject = GUtil.gson.toJsonTree(searcherClient.getFeed(data.get(Constant.DocumentFieldName.CONNECTOME_ID_FIELD).getAsString(), data.get(Constant.DocumentFieldName.DOC_ID_CONTENT_FIELD).getAsString()).getData()).getAsJsonObject();
            }

            feedJsonObject.remove("isBookmarked");
            feedJsonObject.add("isBookmarked", data.get("isBookmarked"));

            redisConnection.saveValueToFeedAsSync(data.get(Constant.DocumentFieldName.CONNECTOME_ID_FIELD).getAsString() + "_" + data.get(Constant.DocumentFieldName.DOC_ID_CONTENT_FIELD).getAsString(), GUtil.gson.toJson(feedJsonObject));

            data.remove(Constant.DocumentFieldName.FEED_PARTITION_FIELD);
            data.add(Constant.DocumentFieldName.FEED_PARTITION_FIELD, feedJsonObject.get(Constant.DocumentFieldName.FEED_PARTITION_FIELD));

            data.remove(Constant.DocumentFieldName.IS_DELETED_FIELD);
            data.remove(Constant.DocumentFieldName.LIKED_FIELD);

            producer.send(KafkaConstant.FEED_UPDATE_TOPIC, GUtil.gson.toJson(data));
            logger.info(String.format("[bookmarkFeed] requestId: %s DONE success", feedModel.getRequestId()));
        } catch (Exception e) {
            response.setStatus(-1, "failed");
            logger.error(String.format("[bookmarkFeed] requestId: %s error unknown" + e.getMessage(), feedModel.getRequestId()), e);
        }
        return response;
    }

    @Override
    public ResponseDocument getDocumentById(RequestBodyGetDocument requestBody) {
        ResponseDocument response = new ResponseDocument();
        try {
            // no connectomeId => data is Doc content
            if (Objects.equals(requestBody.getConnectomeId(), "")) {
                DocContentModel docContentModel = redisConnection.getValueOfFeedContent(requestBody.getDocId());
                // doc content existed in redis
                if (docContentModel != null) {
                    DocContentModel docContentModelFromSearcher = searcherClient.getFeedContent(requestBody.getDocId()).getData();
                    response.success();
                    response.setRequestId(requestBody.getRequestId());
                    response.setData(docContentModelFromSearcher.mergeDocContent(docContentModel));
                    return response;
                } else {
                    // call goSearcher
                    response = searcherClient.getDocumentById(requestBody);
                    redisConnection.saveValueToFeedContentAsSync(requestBody.getDocId(), Utils.gson.toJson(response.getData()));
                }
            } else {
                // require content = false => data is only Document (Not content)
                if (!requestBody.getRequire_content()) {
                    DocModel docModel = redisConnection.getValueOfFeed(requestBody.getConnectomeId() + "_" + requestBody.getDocId());
                    // document existed in redis
                    if (docModel != null) {
                        response.success();
                        response.setRequestId(requestBody.getRequestId());
                        response.setData(docModel);
                        return response;
                    } else {
                        // call goSearcher
                        response = searcherClient.getDocumentById(requestBody);
                        redisConnection.saveValueToFeedAsSync(requestBody.getConnectomeId() + "_" + requestBody.getDocId(), Utils.gson.toJson(response.getData()));
                    }
                }
                // require content = true => data is Document and Content
                else {
                    DocContentModel docContentModel = redisConnection.getValueOfFeedContent(requestBody.getDocId());
                    DocModel docModel = redisConnection.getValueOfFeed(requestBody.getConnectomeId() + "_" + requestBody.getDocId());
                    DocDataModel docDataModel = new DocDataModel();
                    if (docContentModel != null && docModel != null) {
                        docDataModel.buildFeedDataModel(docModel, docContentModel);
                        response.success();
                        response.setRequestId(requestBody.getRequestId());
                        response.setData(docDataModel);
                    } else {
                        // redis save not full (only Doc or only Doc content) => call goSearcher
                        response = searcherClient.getDocumentById(requestBody);
                        docModel = GUtil.gson.fromJson(GUtil.gson.toJson(response.getData()), DocModel.class);
                        docContentModel = GUtil.gson.fromJson(GUtil.gson.toJson(response.getData()), DocContentModel.class);
                        redisConnection.saveValueToFeedAsSync(requestBody.getConnectomeId() + "_" + requestBody.getDocId(), GUtil.gson.toJson(docModel));
                        redisConnection.saveValueToFeedContentAsSync(requestBody.getDocId(), GUtil.gson.toJson(docContentModel));
                    }
                }
            }
        } catch (Exception e) {
            response.setResult("failed");
            response.setResult_code(-1);
            logger.error(String.format("[getListDocumentByIds] requestId: %s error unknown" + e.getMessage(), requestBody.getRequestId()), e);
        }
        return response;
    }

    @Override
    public DataListResponse<?> getListDocumentByIds(RequestBodyGetListDoc requestBody) {

        DataListResponse<?> searcherResponse = new DataListResponse<>(0, "success", requestBody.getRequestId());

//        DataListResponse<?> searcherResponse = searcherClient.getListDocumentByIds(requestBody);
        try {
//            // no connectomeId => data is List Doc content
//            if (Objects.equals(requestBody.getConnectomeId(), "")) {
//                DataListResponse<DocContentModel> response = new DataListResponse<>("success", 0, requestBody.getRequestId(), searcherResponse.getCurrentPage(), searcherResponse.getTotalItems(), searcherResponse.getTotalPages(), null);
//                Map<String, DocContentModel> docContentModelMap = new HashMap<>();
//
//                for (Object data : searcherResponse.getData()) {
//                    DocContentModel docContentModel = GUtil.gson.fromJson(GUtil.gson.toJson(data), DocContentModel.class);
//                    docContentModelMap.put(docContentModel.getDocId(), docContentModel);
//                }
//
//                List<String> keys = new ArrayList<>(docContentModelMap.keySet());
//                List<DocContentModel> docContentModelList = redisConnection.getListValueOfDocContent(keys);
//
//                for (DocContentModel docContentModel : docContentModelList) {
//                    docContentModelMap.put(docContentModel.getDocId(), docContentModel);
//                }
//
//                response.setData(new ArrayList<DocContentModel>(docContentModelMap.values()));
//                return response;
//            } else {
//                // require content = false => data is only List Document (Not content)
//                if (!requestBody.getRequire_content()) {
//                    DataListResponse<DocModel> response = new DataListResponse<>("success", 0, requestBody.getRequestId(), searcherResponse.getCurrentPage(), searcherResponse.getTotalItems(), searcherResponse.getTotalPages(), null);
//                    Map<String, DocModel> docModelHashMap = new HashMap<>();
//
//                    for (Object data : searcherResponse.getData()) {
//                        DocModel docModel = GUtil.gson.fromJson(GUtil.gson.toJson(data), DocModel.class);
//                        docModelHashMap.put(docModel.getConnectomeId() + "_" + docModel.getDocId(), docModel);
//                    }
//
//                    List<String> keys = new ArrayList<>(docModelHashMap.keySet());
//                    List<DocModel> docContentModelList = redisConnection.getListValueOfDoc(keys);
//
//                    for (DocModel docModel : docContentModelList) {
//                        docModelHashMap.put(docModel.getConnectomeId() + "_" + docModel.getDocId(), docModel);
//                    }
//
//                    response.setData(new ArrayList<DocModel>(docModelHashMap.values()));
//                    return response;
//
//                }
//                // require content = true => data is Document and Content
//                else {
//                    DataListResponse<DocDataModel> response = new DataListResponse<>("success", 0, requestBody.getRequestId(), searcherResponse.getCurrentPage(), searcherResponse.getTotalItems(), searcherResponse.getTotalPages(), null);
//                    Map<String, DocModel> docModelHashMap = new HashMap<>();
//                    Map<String, DocContentModel> docContentModelMap = new HashMap<>();
//
//                    for (Object data : searcherResponse.getData()) {
//                        DocModel docModel = GUtil.gson.fromJson(GUtil.gson.toJson(data), DocModel.class);
//                        DocContentModel docContentModel = GUtil.gson.fromJson(GUtil.gson.toJson(data), DocContentModel.class);
//                        docModelHashMap.put(docModel.getConnectomeId() + "_" + docModel.getDocId(), docModel);
//                        docContentModelMap.put(docContentModel.getDocId(), docContentModel);
//                    }
//
//                    List<String> keys = new ArrayList<>(docModelHashMap.keySet());
//                    List<DocModel> docModelList = redisConnection.getListValueOfDoc(keys);
//                    List<String> contentKeys = new ArrayList<>(docContentModelMap.keySet());
//                    List<DocContentModel> docContentModelList = redisConnection.getListValueOfDocContent(contentKeys);
//
//                    if (docModelList != null && docModelList.size() > 0) {
//                        for (DocModel docModel : docModelList) {
//                            docModelHashMap.put(docModel.getConnectomeId() + "_" + docModel.getDocId(), docModel);
//                        }
//                    }
//                    if (docContentModelList != null && docContentModelList.size() > 0) {
//                        for (DocContentModel docContentModel : docContentModelList) {
//                            docContentModelMap.put(docContentModel.getDocId(), docContentModel);
//                        }
//                    }
//
//                    List<DocModel> docModels = new ArrayList<DocModel>(docModelHashMap.values());
//
//                    List<DocDataModel> docDataModels = new ArrayList<DocDataModel>();
//
//                    for (DocModel docModel : docModels) {
//                        DocDataModel docDataModel = new DocDataModel();
//                        docDataModel.buildFeedDataModel(docModel, docContentModelMap.get(docModel.getDocId()));
//                        docDataModels.add(docDataModel);
//                    }
//
//                    response.setData(docDataModels);
//                    return response;
//                }
//
//            }

            Callable<DataListResponse<?>> callES = new Callable<DataListResponse<?>>() {
                @Override
                public DataListResponse<?> call() throws Exception {
                    DataListResponse<?> response = new DataListResponse<>();
                    try {
                        return searcherClient.getListDocumentByIds(requestBody);
                    } catch (Exception e) {
                        logger.error(String.format("requestId: %s, connectomeId: %s, docIds : %s error search ES: %s", requestBody.getRequestId(), requestBody.getConnectomeId(), requestBody.getDocIds(), e.getMessage()));
                    }
                    return response;
                }
            };

            if (requestBody.getSize() == null || requestBody.getPage() == null || requestBody.getDocIds().size() < 2 * requestBody.getSize()) {
                if (Objects.equals(requestBody.getConnectomeId(), "")) {
                    Future<DataListResponse<?>> dataFromESFuture = executorService.submit(callES);

                    DataListResponse<DocContentModel> response = new DataListResponse<>();

                    Callable<List<DocContentModel>> callRedis = new Callable<List<DocContentModel>>() {
                        @Override
                        public List<DocContentModel> call() throws Exception {
                            List<DocContentModel> results = new LinkedList<>();
                            try {
                                results.addAll(redisConnection.getListValueOfDocContent(requestBody.getDocIds()));
                            } catch (Exception e) {
                                logger.error(String.format("requestId: %s, connectomeId: %s, docIds : %s error search Redis: %s", requestBody.getRequestId(), requestBody.getConnectomeId(), requestBody.getDocIds(), e.getMessage()));
                            }
                            return results;
                        }
                    };

                    Future<List<DocContentModel>> dataFromRedisFuture = executorService.submit(callRedis);
                    List<DocContentModel> resultData = new LinkedList<>();
                    Map<String, DocContentModel> docContentModelMapFromRedis = new HashMap<>();

                    DataListResponse<?> dataFromES = dataFromESFuture.get(5, TimeUnit.SECONDS);
                    List<DocContentModel> dataFromRedis = dataFromRedisFuture.get(5, TimeUnit.SECONDS);

                    for (DocContentModel docContentModel : dataFromRedis) {
                        docContentModelMapFromRedis.put(docContentModel.getDocId(), docContentModel);
                    }

                    for (Object docContentJson : dataFromES.getData()) {
                        DocContentModel docContentModel = GUtil.gson.fromJson(GUtil.gson.toJson(docContentJson), DocContentModel.class);
                        docContentModel.mergeDocContent(docContentModelMapFromRedis.get(docContentModel.getDocId()));
                        resultData.add(docContentModel);
                    }

                    response.setData(resultData);
                    response.setStatus(0, "success", requestBody.getRequestId());
                    return response;
                } else if (!requestBody.getRequire_content()) {
                    Future<DataListResponse<?>> dataFromESFuture = executorService.submit(callES);

                    DataListResponse<DocModel> response = new DataListResponse<>();
                    Callable<List<DocModel>> callRedis = new Callable<List<DocModel>>() {
                        @Override
                        public List<DocModel> call() throws Exception {
                            List<DocModel> results = new LinkedList<>();
                            try {
                                results.addAll(redisConnection.getListValueOfDoc(requestBody.getDocIds()));
                            } catch (Exception e) {
                                logger.error(String.format("requestId: %s, connectomeId: %s, docIds : %s error search Redis: %s", requestBody.getRequestId(), requestBody.getConnectomeId(), requestBody.getDocIds(), e.getMessage()));
                            }
                            return results;
                        }
                    };

                    Future<List<DocModel>> dataFromRedisFuture = executorService.submit(callRedis);
                    List<DocModel> resultData = new LinkedList<>();
                    Map<String, DocModel> docModelMapFromRedis = new HashMap<>();

                    DataListResponse<?> dataFromES = dataFromESFuture.get(5, TimeUnit.SECONDS);
                    List<DocModel> dataFromRedis = dataFromRedisFuture.get(5, TimeUnit.SECONDS);

                    for (DocModel docModel : dataFromRedis) {
                        docModelMapFromRedis.put(docModel.getDocId(), docModel);
                    }

                    for (Object docContentJson : dataFromES.getData()) {
                        DocModel docModel = GUtil.gson.fromJson(GUtil.gson.toJson(docContentJson), DocModel.class);
                        docModel.mergeDoc(docModelMapFromRedis.get(docModel.getDocId()));
                        resultData.add(docModel);
                    }

                    response.setData(resultData);
                    response.setStatus(0, "success", requestBody.getRequestId());
                    return response;
                } else {
                    Future<DataListResponse<?>> dataFromESFuture = executorService.submit(callES);
                    DataListResponse<DocDataModel> response = new DataListResponse<>();

                    List<String> docKeys = new LinkedList<>();
                    for (String docId : requestBody.getDocIds()) {
                        docKeys.add(requestBody.getConnectomeId() + "_" + docId);
                    }

                    Map<String, DocModel> docModelHashMap = new HashMap<>();
                    Map<String, DocContentModel> docContentModelMap = new HashMap<>();
                    List<DocDataModel> docDataModels = new ArrayList<>();

                    RedisFuture<List<KeyValue<String, String>>> futureFromRedis = redisConnection.getListValueOfDocAsync(docKeys);
                    RedisFuture<List<KeyValue<String, String>>> contentFutureFromRedis = redisConnection.getListValueOfDocContentAsync(requestBody.getDocIds());

                    List<KeyValue<String, String>> dataFromRedis = futureFromRedis.get(5, TimeUnit.SECONDS);
                    for (KeyValue<String, String> keyValue : dataFromRedis) {
                        try{
                            DocModel docModel = GUtil.gson.fromJson(keyValue.getValue(), DocModel.class);
                            docModelHashMap.put(docModel.getConnectomeId() + "_" + docModel.getDocId(), docModel);
                        } catch (NoSuchElementException e){
                            //todo nothing
                            logger.info(String.format("Redis don't save data with Key: %s , message: %s", keyValue.getKey(), e.getMessage()));
                        }
                    }

                    for (KeyValue<String, String> keyValue : contentFutureFromRedis.get(5, TimeUnit.SECONDS)) {
                        try {
                            DocContentModel docContentModel = GUtil.gson.fromJson(keyValue.getValue(), DocContentModel.class);
                            docContentModelMap.put(docContentModel.getDocId(), docContentModel);
                        } catch (NoSuchElementException e){
                            logger.info(String.format("Redis don't save data with Key: %s , message: %s", keyValue.getKey(), e.getMessage()));
                        }
                    }

                    for (Object dataFromES : dataFromESFuture.get(5, TimeUnit.SECONDS).getData()) {
                        DocDataModel dataModel = GUtil.gson.fromJson(GUtil.gson.toJson(dataFromES), DocDataModel.class);
                        dataModel.mergeDocData(GUtil.gson.fromJson(GUtil.gson.toJson(docModelHashMap.get(dataModel.getConnectomeId() + "_" + dataModel.getDocId())), DocDataModel.class));
                        dataModel.mergeDocData(GUtil.gson.fromJson(GUtil.gson.toJson(docContentModelMap.get(dataModel.getDocId())), DocDataModel.class));
                        docDataModels.add(dataModel);
                    }

                    response.setData(docDataModels);
                    response.setStatus(0, "success", requestBody.getRequestId());
                    return response;

                }
            } else {
                if (requestBody.getConnectomeId() == null) {
                    DataListResponse<?> responseFromES = searcherClient.getListDocumentByIds(requestBody);

                    Map<String, DocContentModel> docContentModelMap = new HashMap<>();

                    for (Object data : responseFromES.getData()) {
                        DocContentModel docContentModel = GUtil.gson.fromJson(GUtil.gson.toJson(data), DocContentModel.class);
                        docContentModelMap.put(docContentModel.getDocId(), docContentModel);
                    }

                    List<DocContentModel> docContentModels = redisConnection.getListValueOfDocContent(new ArrayList<>(docContentModelMap.keySet()));

                    for (DocContentModel docContentModel : docContentModels) {
                        docContentModelMap.put(docContentModel.getDocId(), docContentModelMap.get(docContentModel.getDocId()).mergeDocContent(docContentModel));
                    }

                    List<DocContentModel> responseData = new ArrayList<>(docContentModelMap.values());

                    DataListResponse<DocContentModel> response = new DataListResponse<>();
                    response.setData(responseData);
                    response.mergeResponse(responseFromES);

                    return response;
                } else if (!requestBody.getRequire_content()) {
                    DataListResponse<?> responseFromES = searcherClient.getListDocumentByIds(requestBody);

                    Map<String, DocModel> docModelMap = new HashMap<>();

                    for (Object data : responseFromES.getData()) {
                        DocModel docModel = GUtil.gson.fromJson(GUtil.gson.toJson(data), DocModel.class);
                        docModelMap.put(docModel.getConnectomeId() + "_" + docModel.getDocId(), docModel);
                    }

                    List<DocModel> docContentModels = redisConnection.getListValueOfDoc(new ArrayList<>(docModelMap.keySet()));

                    for (DocModel docModel : docContentModels) {
                        docModelMap.put(docModel.getConnectomeId() + "_" + docModel.getDocId(), docModelMap.get(docModel.getConnectomeId() + "_" + docModel.getDocId()).mergeDoc(docModel));
                    }

                    List<DocModel> responseData = new ArrayList<>(docModelMap.values());

                    DataListResponse<DocModel> response = new DataListResponse<>();
                    response.setData(responseData);
                    response.mergeResponse(responseFromES);

                    return response;
                } else {
                    DataListResponse<?> responseFromES = searcherClient.getListDocumentByIds(requestBody);

                    Map<String, DocContentModel> docContentModelMap = new HashMap<>();
                    Map<String, DocModel> docModelMapFromES = new HashMap<>();
                    Map<String, DocDataModel> docDataModelMapFromES = new HashMap<>();

                    for (Object data : responseFromES.getData()) {
                        String dataJson = GUtil.gson.toJson(data);

                        DocContentModel docContentModel = GUtil.gson.fromJson(dataJson, DocContentModel.class);
                        DocModel docModel = GUtil.gson.fromJson(dataJson, DocModel.class);

                        docContentModelMap.put(docContentModel.getDocId(), docContentModel);
                        docModelMapFromES.put(docModel.getConnectomeId() + "_" + docModel.getDocId(), docModel);

                        DocDataModel docDataModel = new DocDataModel();
                        docDataModel.buildFeedDataModel(docModel,docContentModel);
                        docDataModelMapFromES.put(docModel.getConnectomeId() + "_" + docModel.getDocId(), docDataModel);
                    }

                    Future<List<KeyValue<String, String>>> docModelFutureFromRedis = redisConnection.getListValueOfDocAsync(new ArrayList<>(docModelMapFromES.keySet()));
                    Future<List<KeyValue<String, String>>> docContentModelFutureFromRedis = redisConnection.getListValueOfDocContentAsync(new ArrayList<>(docContentModelMap.keySet()));

                    Map<String, DocContentModel> docContentModelMapFromRedis = new HashMap<>();

                    for (KeyValue<String,String> keyValue : docContentModelFutureFromRedis.get()){
                        try {
                            DocContentModel docContentModel = GUtil.gson.fromJson(keyValue.getValue(),DocContentModel.class);
                            docContentModelMapFromRedis.put(docContentModel.getDocId(),docContentModel);
                        } catch (NoSuchElementException e){
                            //todo nothing
                            logger.info(String.format("Redis don't save data with Key: %s , message: %s", keyValue.getKey(), e.getMessage()));
                        }
                    }

                    for(KeyValue<String, String> keyValue : docModelFutureFromRedis.get()){
                        try {
                            DocModel docModel = GUtil.gson.fromJson(keyValue.getValue(), DocModel.class);

                            DocDataModel docDataModelFromRedis = new DocDataModel();
                            docDataModelFromRedis.buildFeedDataModel(docModel, docContentModelMapFromRedis.get(docModel.getDocId()));

                            DocDataModel dataModel = docDataModelMapFromES.get(docModel.getConnectomeId()+"_"+docModel.getDocId());
                            dataModel.mergeDocData(docDataModelFromRedis);

                            docDataModelMapFromES.put(docModel.getConnectomeId()+"_"+docModel.getDocId(), dataModel);
                        } catch (NoSuchElementException e){
                            //todo nothing
                            logger.info(String.format("Redis don't save data with Key: %s , message: %s", keyValue.getKey(), e.getMessage()));
                        }
                    }

                    List<DocDataModel> responseData = new ArrayList<>(docDataModelMapFromES.values());

                    DataListResponse<DocDataModel> response = new DataListResponse<>();
                    response.setData(responseData);
                    response.mergeResponse(responseFromES);

                    return response;
                }
            }
        } catch (Exception e) {
            searcherResponse.setStatus(-1, "failed");
            logger.error(String.format("[getListDocumentByIds] requestId: %s error unknown" + e.getMessage(), requestBody.getRequestId()), e);
        }
        return searcherResponse;
    }
}
