package com.saltlux.deepsignal.feedcache.service;

import com.saltlux.deepsignal.feedcache.api.client.SearcherClient;
import com.saltlux.deepsignal.feedcache.model.FeedContentModel;
import com.saltlux.deepsignal.feedcache.model.FeedModel;
import com.saltlux.deepsignal.feedcache.redis.RedisConnection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class FeedServiceTest {
    @Value("${spring.application.name}")
    private String name;
    @Autowired
    private RedisConnection redisConnection;
    @Autowired
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
}