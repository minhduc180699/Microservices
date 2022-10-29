package com.saltlux.deepsignal.feedcache.service;

import com.saltlux.deepsignal.feedcache.api.client.SearcherClient;
import com.saltlux.deepsignal.feedcache.model.DocContentModel;
import com.saltlux.deepsignal.feedcache.model.DocModel;
import com.saltlux.deepsignal.feedcache.redis.RedisConnection;
import com.saltlux.deepsignal.feedcache.utils.GUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FeedServiceTest {
    @Value("${spring.application.name}")
    private String name;
    @Autowired
    private RedisConnection redisConnection;
    @Autowired
    @Qualifier("com.saltlux.deepsignal.feedcache.api.client.SearcherClient")
    private SearcherClient searcherClient;
    @Test
    void getListFeedData() {

    }

    @Test
    void searchFeedData() {

    }

    @Test
    void getFeed() {
    }

    @Test
    void createFeed() {

    }

    @Test
    void updateFeed() {
    }

    @Test
    void likeFeed() {
    }

    @Test
    void hideFeed() {
    }

    @Test
    void bookmarkFeed() {
    }

    @Test
    void test(){
        DocModel docModel = searcherClient.getFeed("CID_adae75ca-e288-40b5-9632-5a7b10d19e9f", "ds-global-web-eng-6322215311985902446-0-0").getData();
        redisConnection.saveValueToFeedAsSync(docModel.getConnectomeId()+"_"+ docModel.getDocId(), GUtil.gson.toJson(docModel));
        System.out.println(redisConnection.getValueOfFeedTypeJsonObject(docModel.getConnectomeId()+"_"+ docModel.getDocId()));

        DocContentModel docContentModel = searcherClient.getFeedContent("ds-global-web-eng-6322215311985902446-0-0").getData();
        redisConnection.saveValueToFeedContentAsSync(docContentModel.getDocId(), GUtil.gson.toJson(docContentModel));
        System.out.println(GUtil.gson.toJson(docContentModel));
    }
}