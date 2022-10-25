package com.saltlux.deepsignal.feedcache.test;

import com.google.gson.JsonObject;
import com.saltlux.deepsignal.feedcache.DsFeedCacheApiApplication;
import com.saltlux.deepsignal.feedcache.constant.KafkaConstant;
import com.saltlux.deepsignal.feedcache.dto.ResultResponse;
import com.saltlux.deepsignal.feedcache.model.FeedModel;
import com.saltlux.deepsignal.feedcache.redis.RedisConnection;
import com.saltlux.deepsignal.feedcache.utils.GUtil;
import org.apache.commons.codec.digest.MurmurHash3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;

public class Test {

    @Autowired
    private static RedisConnection redisConnection;
    public static void main(String[] args) {
        SpringApplication.run(DsFeedCacheApiApplication.class, args);
        FeedModel feedModel = new FeedModel();
        feedModel.set_id("ds-feed-6348e7782939b6198c14b3d2-0-0");
        bookmarkFeed(feedModel);
    }

    public static ResultResponse bookmarkFeed(FeedModel feedModel) {
        ResultResponse response = new ResultResponse(0, "success", feedModel.getRequestId());
        try {
            JsonObject data = GUtil.gson.toJsonTree(feedModel).getAsJsonObject();
            JsonObject feedJsonObject = redisConnection.getValueOfFeedTypeJsonObject(data.get("_id").getAsString());
            data.add("feed_partition", feedJsonObject.get("feed_partition"));
            System.out.println(data);
        }catch (Exception e){
            response.setStatus(-1, "failed");
        }
        return response;
    }
}
