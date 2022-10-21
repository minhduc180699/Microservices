package com.saltlux.deepsignal.feedcache.service;

import com.google.gson.JsonObject;
import com.saltlux.deepsignal.feedcache.api.client.RealtimeCrawlerClient;
import com.saltlux.deepsignal.feedcache.api.client.SearcherClient;
import com.saltlux.deepsignal.feedcache.constant.KafkaConstant;
import com.saltlux.deepsignal.feedcache.dto.DataListResponse;
import com.saltlux.deepsignal.feedcache.dto.DataResponse;
import com.saltlux.deepsignal.feedcache.dto.FeedDto;
import com.saltlux.deepsignal.feedcache.dto.ResultResponse;
import com.saltlux.deepsignal.feedcache.kafka.Producer;
import com.saltlux.deepsignal.feedcache.model.FeedContentModel;
import com.saltlux.deepsignal.feedcache.model.FeedDataModel;
import com.saltlux.deepsignal.feedcache.model.FeedModel;
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
    public DataListResponse<FeedModel> getListFeed(String connectomeId, String request_id, Integer page, Integer size) {
        DataListResponse<FeedModel> response = new DataListResponse<>(0, "success", request_id);
        List<FeedModel> feedModelList = null;
        try{

            feedModelList = searcherClient.getListFeed(connectomeId, page, size).getData();
            if (feedModelList != null && !feedModelList.isEmpty()){
                for (FeedModel feedModel : feedModelList){

                    // Save in Redis
                    redisConnection.saveValueToFeedAsSync(feedModel.get_id(), GUtil.gson.toJson(feedModel));

                }
                logger.info(String.format("[getListFeedData] requestId: %s DONE success", request_id));
            }

        }catch (Exception e){
            response.setStatus(-1, "failed");
            logger.error(String.format("[getListFeedData] requestId: %s error unknown" + e.getMessage(), request_id), e);
        }
        response.setData(feedModelList);
        return response;
    }

    @Override
    public DataListResponse<FeedModel> getListDocumentByIds(FeedDto feedDto) {
        DataListResponse<FeedModel> response = new DataListResponse<>(0, "success", feedDto.getRequest_id());
        try {
            List<FeedModel> feedModelList;
            feedModelList = searcherClient.getListDocumentByIds(feedDto).getData();
            response.setData(feedModelList);
        }catch (Exception e){
            response.setStatus(-1, "failed");
            logger.error(String.format("[getListDocumentByIds] requestId: %s error unknown" + e.getMessage(), feedDto.getRequest_id()), e);
        }
        return response;
    }

    @Override
    public DataListResponse<FeedModel> searchFeed(String connectomeId, String request_id, String keyword, String from, String until, Integer page, Integer size, String searchType, String channels, String lang, String type) {
        DataListResponse<FeedModel> response = new DataListResponse<>(0, "success", request_id);
        List<FeedModel> feedDataModelList = null;

        try{
            // Search fee data from Elasticsearch
            feedDataModelList = searcherClient.searchFeed(connectomeId, keyword, from, until, page, size, searchType, channels, lang, type).getData();
            logger.info(String.format("[searchFeedData] requestId: %s DONE success", request_id));
        }catch (Exception e){
            response.setStatus(-1, "failed");
            logger.error(String.format("[searchFeedData] requestId: %s error unknown" + e.getMessage(), request_id), e);
        }
        response.setData(feedDataModelList);
        return response;
    }

    @Override
    public DataResponse<FeedDataModel> getFeed(String connectomeId, String request_id, String _id) {
        DataResponse<FeedDataModel> response = new DataResponse<>(0, "success", request_id);
        FeedDataModel feedDataModel = new FeedDataModel();

        try {
            // Get feed from redis
            FeedModel feedModel = redisConnection.getValueOfFeed(_id);
            FeedContentModel feedContentModel;
            // Has feed
            if (feedModel != null) {
                // Check feed content in redis
                feedContentModel = redisConnection.getValueOfFeedContent(feedModel.getDocId_content());
                // Has not feed content: get feed content from Elasticsearch
                if (feedContentModel == null){
                    feedContentModel = searcherClient.getFeedContent(feedModel.getDocId_content()).getData();
                    if (feedContentModel != null){
                        redisConnection.saveValueToFeedContentAsSync(feedContentModel.get_id(), GUtil.gson.toJson(feedContentModel));
                    }
                }
            }
            // Has not feed: Get feed & feed content from Elasticsearch
            else{
                feedModel = searcherClient.getFeed(_id).getData();
                feedContentModel = searcherClient.getFeedContent(feedModel.getDocId_content()).getData();
                // Execute task save feed into Redis
                if (feedModel != null) {
                    redisConnection.saveValueToFeedAsSync(feedModel.get_id(), GUtil.gson.toJson(feedModel));
                }
                if (feedContentModel != null){
                    redisConnection.saveValueToFeedContentAsSync(feedContentModel.get_id(), GUtil.gson.toJson(feedContentModel));
                }

            }

            feedDataModel.buildFeedDataModel(feedModel, feedContentModel);
            logger.info(String.format("[getFeed] requestId: %s DONE success", request_id));
        }catch (Exception e){
            response.setStatus(-1, "failed");
            logger.error(String.format("[getFeed] requestId: %s error unknown" + e.getMessage(), request_id), e);
        }
        response.setData(feedDataModel);
        return response;
    }

    @Override
    public ResultResponse createFeed(FeedDataModel data) {
        ResultResponse response = new ResultResponse(0, "success", data.getRequestId());
        try {
            LinkedHashMap<String, Object> saveRecord = new LinkedHashMap<>();
            List<String> keyField = new ArrayList<>();
            String sourceId = data.getSource_uri() + "-" + data.getComment_id() + "-" + data.getReply_id();
            saveRecord.put("source_id", sourceId);
            keyField.add("source_id");
            Long uniqueKey = Utils.getHashCode(saveRecord, keyField);
            data.set__unique__key(uniqueKey);
//            producer.send(KafkaConstant.FEED_CREATE_TOPIC, GUtil.gson.toJson(data));
            if (data.getContent() == null || data.getContent().isEmpty()){
                realtimeCrawlerClient.postRealtimeCrawler(Collections.singletonList(data.toFeedRealtimeCrawlerModel()));
            }
            logger.info(String.format("[createFeed] requestId: %s DONE success", data.getRequestId()));
        }catch (Exception e){
            response.setStatus(-1, "failed");
            logger.error(String.format("[createFeed] requestId: %s error unknown" + e.getMessage(), data.getRequestId()), e);
        }
        return response;
    }

    @Override
    public ResultResponse updateFeed(FeedModel feedModel) {

        ResultResponse response = new ResultResponse(0, "success", feedModel.getRequestId());
        try {
            JsonObject data = GUtil.gson.toJsonTree(feedModel).getAsJsonObject();
            JsonObject feedFromRedis = redisConnection.getValueOfFeedTypeJsonObject(data.get("_id").getAsString());
            if (feedFromRedis != null) {
                for (String key : data.keySet()){
                    feedFromRedis.remove(key);
                    feedFromRedis.add(key, data.get(key));
                }
                redisConnection.saveValueToFeedAsSync(data.get("_id").getAsString(), GUtil.gson.toJson(feedFromRedis));
            }
            producer.send(KafkaConstant.FEED_UPDATE_TOPIC, GUtil.gson.toJson(data));
            logger.info(String.format("[updateFeed] requestId: %s DONE success", feedModel.getRequestId()));
        }catch (Exception e){
            response.setStatus(-1, "failed");
            logger.error(String.format("[updateFeed] requestId: %s error unknown" + e.getMessage(), feedModel.getRequestId()), e);
        }
        return response;
    }

    @Override
    public ResultResponse likeFeed(FeedModel feedModel) {
        ResultResponse response = new ResultResponse(0, "success", feedModel.getRequestId());
        try {
            JsonObject data = GUtil.gson.toJsonTree(feedModel).getAsJsonObject();
            JsonObject feedJsonObject = redisConnection.getValueOfFeedTypeJsonObject(data.get("_id").getAsString());
            if (feedJsonObject != null) {
                feedJsonObject.remove("liked");
                feedJsonObject.add("liked", data.get("liked"));

                redisConnection.saveValueToFeedAsSync(data.get("_id").getAsString(), GUtil.gson.toJson(feedJsonObject));
            }
            producer.send(KafkaConstant.FEED_UPDATE_TOPIC, GUtil.gson.toJson(data));
            logger.error(String.format("[likeFeed] requestId: %s DONE success", feedModel.getRequestId()));
        }catch (Exception e){
            response.setStatus(-1, "failed");
            logger.info(String.format("[likeFeed] requestId: %s error unknown" + e.getMessage(), feedModel.getRequestId()), e);
        }
        return response;
    }

    @Override
    public ResultResponse hideFeed(FeedModel feedModel) {
        ResultResponse response = new ResultResponse(0, "success", feedModel.getRequestId());
        try {
            JsonObject data = GUtil.gson.toJsonTree(feedModel).getAsJsonObject();
            JsonObject feedJsonObject = redisConnection.getValueOfFeedTypeJsonObject(data.get("_id").getAsString());
            if (feedJsonObject != null) {
                feedJsonObject.remove("isDeleted");
                feedJsonObject.add("isDeleted", data.get("isDeleted"));
                redisConnection.saveValueToFeedAsSync(data.get("_id").getAsString(), GUtil.gson.toJson(feedJsonObject));
            }
            producer.send(KafkaConstant.FEED_UPDATE_TOPIC, GUtil.gson.toJson(data));
            logger.info(String.format("[hideFeed] requestId: %s DONE success", feedModel.getRequestId()));
        }catch (Exception e){
            response.setStatus(-1, "failed");
            logger.error(String.format("[hideFeed] requestId: %s error unknown" + e.getMessage(), feedModel.getRequestId()), e);
        }
        return response;
    }

    @Override
    public ResultResponse bookmarkFeed(FeedModel feedModel) {
        ResultResponse response = new ResultResponse(0, "success", feedModel.getRequestId());
        try {
            JsonObject data = GUtil.gson.toJsonTree(feedModel).getAsJsonObject();
            JsonObject feedJsonObject = redisConnection.getValueOfFeedTypeJsonObject(data.get("_id").getAsString());
            if (feedJsonObject != null) {
                feedJsonObject.remove("isBookmarked");
                feedJsonObject.add("isBookmarked", data.get("isBookmarked"));
                redisConnection.saveValueToFeedAsSync(data.get("_id").getAsString(), GUtil.gson.toJson(feedJsonObject));
            }
            producer.send(KafkaConstant.FEED_UPDATE_TOPIC, GUtil.gson.toJson(data));
            logger.info(String.format("[bookmarkFeed] requestId: %s DONE success", feedModel.getRequestId()));
        }catch (Exception e){
            response.setStatus(-1, "failed");
            logger.error(String.format("[bookmarkFeed] requestId: %s error unknown" + e.getMessage(), feedModel.getRequestId()), e);
        }
        return response;
    }
}
