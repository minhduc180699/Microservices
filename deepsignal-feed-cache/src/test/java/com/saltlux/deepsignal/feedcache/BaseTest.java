package com.saltlux.deepsignal.feedcache;

import com.saltlux.deepsignal.feedcache.model.FeedMetaSearchModel;
import com.saltlux.deepsignal.feedcache.utils.GUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
public class BaseTest {
    @Test
    public void testGson(){
        String a = "{\"title\":\"a\"}";
        FeedMetaSearchModel feedMetaSearchModel = GUtil.gson.fromJson(a, FeedMetaSearchModel.class);
        String b = "";
    }
}
