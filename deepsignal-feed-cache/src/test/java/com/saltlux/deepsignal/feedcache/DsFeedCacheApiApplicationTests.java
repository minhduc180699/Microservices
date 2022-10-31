package com.saltlux.deepsignal.feedcache;

//import com.saltlux.deepsignal.feedcache.redis.RedisConnection;
import com.saltlux.deepsignal.feedcache.model.DocModel;
import com.saltlux.deepsignal.feedcache.redis.RedisConnection;
import com.saltlux.deepsignal.feedcache.utils.GUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DsFeedCacheApiApplicationTests {
    @Autowired
    RedisConnection redisConnection;
    @Test
    void checkRedis() {
        String _id = "";
        DocModel docModel = redisConnection.getValueOfFeed(_id);
        System.out.println(GUtil.gson.toJson(docModel));
    }
    @Test
    void checkElasticsearch() {
        String _id = "";
        DocModel docModel = redisConnection.getValueOfFeed(_id);
        System.out.println(GUtil.gson.toJson(docModel));
    }
    @Test
    void testString() {
        String _id = "";
        System.out.println(_id.replace("x","c"));
        System.out.println(_id.replace("x","c"));
    }
}
