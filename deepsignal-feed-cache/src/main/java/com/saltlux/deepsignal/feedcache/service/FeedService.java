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
import com.saltlux.deepsignal.feedcache.utils.GUtil;
import com.saltlux.deepsignal.feedcache.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class FeedService implements IFeedService {
    private Logger logger = LoggerFactory.getLogger(FeedService.class);
    private ExecutorService executorService = Executors.newFixedThreadPool(2);
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
                    redisConnection.saveValueToFeedAsSync(connectomeId + docModel.getDocId(), GUtil.gson.toJson(docModel));

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
    public DataListResponse<DocModel> searchFeed(String connectomeId, String request_id, String keyword, String from, String until, Integer page, Integer size, String searchType, String channels, String lang, String type) {
        DataListResponse<DocModel> response = new DataListResponse<>(0, "success", request_id);

        try {
            // Search fee data from Elasticsearch
            response = searcherClient.searchFeed(connectomeId, keyword, from, until, page, size, searchType, channels, lang, type);
            List<DocModel> docModelList = response.getData();
            for (DocModel docModel : docModelList) {
                redisConnection.saveValueToFeedAsSync(docModel.getConnectomeId() + docModel.getDocId(), GUtil.gson.toJson(docModel));
            }
            logger.info(String.format("[searchFeedData] requestId: %s DONE success", request_id));
        } catch (Exception e) {
            response.setStatus(-1, "failed");
            logger.error(String.format("[searchFeedData] requestId: %s error unknown" + e.getMessage(), request_id), e);
        }
        return response;
    }

    @Override
    public DataResponse<FeedDataResponse> getFeed(String connectomeId, String request_id, String docId) {
        DataResponse<FeedDataResponse> response = new DataResponse<>(0, "success", request_id);
        FeedDataResponse feedDataResponse = new FeedDataResponse();

        try {
            // Get feed from redis
            DocModel docModel = redisConnection.getValueOfFeed(connectomeId + docId);
            DocContentModel docContentModel = null;
            // Has feed
            if (docModel != null) {
                // Check feed content in redis
                docContentModel = redisConnection.getValueOfFeedContent(docId);
                // Has not feed content: get feed content from Elasticsearch
                if (docContentModel == null) {
                    docContentModel = searcherClient.getFeedContent(docModel.getDocId()).getData();
                    if (docContentModel != null) {
                        redisConnection.saveValueToFeedContentAsSync(docContentModel.getDocId(), GUtil.gson.toJson(docContentModel));
                    }
                }
            }
            // Has not feed: Get feed & feed content from Elasticsearch
            else {
                docModel = searcherClient.getFeed(connectomeId, docId).getData();
                docContentModel = searcherClient.getFeedContent(docId).getData();
                // Execute task save feed into Redis
                if (docModel != null) {
                    redisConnection.saveValueToFeedAsSync(docModel.getConnectomeId() + docModel.getDocId(), GUtil.gson.toJson(docModel));
                }
                if (docContentModel != null) {
                    redisConnection.saveValueToFeedContentAsSync(docContentModel.getDocId(), GUtil.gson.toJson(docContentModel));
                }

            }

            feedDataResponse.buildFeedDataModel(docModel, docContentModel);
            logger.info(String.format("[getFeed] requestId: %s DONE success", request_id));
        } catch (Exception e) {
            response.setStatus(-1, "failed");
            logger.error(String.format("[getFeed] requestId: %s error unknown" + e.getMessage(), request_id), e);
        }
        response.setData(feedDataResponse);
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
                    redisConnection.saveValueToFeedAsSync(connectomeId + docModel.getDocId(), GUtil.gson.toJson(docModel));

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
    public ResultResponse createFeed(DocDataModel data) {
        ResultResponse response = new ResultResponse(0, "success", data.getRequestId());
        try {
            LinkedHashMap<String, Object> saveRecord = new LinkedHashMap<>();
            List<String> keyField = new ArrayList<>();
            String sourceId = null;

            if (data.getComment_id() != null && data.getReply_id() != null) {
                sourceId = data.getSource_uri() + "-" + data.getComment_id() + "-" + data.getReply_id();
            } else if (data.getComment_id() == null) {
                sourceId = data.getSource_uri() + "-0-" + data.getReply_id();
            } else if (data.getReply_id() == null) {
                sourceId = data.getSource_uri() + "-" + data.getComment_id() + "-0";
            } else {
                sourceId = data.getSource_uri() + "-0-0";
            }

            saveRecord.put("source_id", sourceId);
            keyField.add("source_id");
            Long uniqueKey = Utils.getHashCode(saveRecord, keyField);
            data.set__unique__key(uniqueKey);
            data.setCollector("RECOMMENDATION");
            producer.send(KafkaConstant.FEED_CREATE_TOPIC, GUtil.gson.toJson(data));
            producer.send(KafkaConstant.FEED_CREATE_TOPIC_TEST, GUtil.gson.toJson(data));
            if ((data.getContent() == null || data.getContent().isEmpty()) && !data.getSource_uri().contains("youtube")) {
                realtimeCrawlerClient.postRealtimeCrawler(Collections.singletonList(data.toFeedRealtimeCrawlerModel()));
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
                    response.success();
                    response.setRequestId(requestBody.getRequestId());
                    response.setData(docContentModel);
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
        DataListResponse<?> searcherResponse = searcherClient.getListDocumentByIds(requestBody);
        try {

            // no connectomeId => data is List Doc content
            if (Objects.equals(requestBody.getConnectomeId(), "")) {
                DataListResponse<DocContentModel> response = new DataListResponse<>(0,"success", requestBody.getRequestId());
                Map<String, DocContentModel> docContentModelMap = new HashMap<>();

                for(Object data : searcherResponse.getData()){
                    DocContentModel docContentModel = GUtil.gson.fromJson(GUtil.gson.toJson(data), DocContentModel.class);
                    docContentModelMap.put(docContentModel.getDocId(), docContentModel);
                }

                List<String> keys = new ArrayList<>(docContentModelMap.keySet());
                List<DocContentModel> docContentModelList = redisConnection.getListValueOfDocContent(keys);

                for(DocContentModel docContentModel : docContentModelList){
                    docContentModelMap.put(docContentModel.getDocId(), docContentModel);
                }

                response.setData(new ArrayList<DocContentModel>(docContentModelMap.values()));
                return response;
            } else {
                // require content = false => data is only List Document (Not content)
                if (!requestBody.getRequire_content()) {


                }
                // require content = true => data is Document and Content
                else {

                }
            }
            return searcherClient.getListDocumentByIds(requestBody);
        } catch (Exception e) {
            searcherResponse.setStatus(-1, "failed");
            logger.error(String.format("[getListDocumentByIds] requestId: %s error unknown" + e.getMessage(), requestBody.getRequestId()), e);
        }
        return searcherResponse;
    }
}
