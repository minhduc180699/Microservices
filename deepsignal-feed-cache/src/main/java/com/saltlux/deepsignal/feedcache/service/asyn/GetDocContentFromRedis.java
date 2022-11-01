package com.saltlux.deepsignal.feedcache.service.asyn;

import com.saltlux.deepsignal.feedcache.model.DocContentModel;
import com.saltlux.deepsignal.feedcache.redis.RedisConnection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetDocContentFromRedis implements Callable<DocContentModel> {

    private String key;

    private RedisConnection redisConnection;

    @Override
    public DocContentModel call() throws Exception {
        return redisConnection.getValueOfFeedContent(key);
    }
}
