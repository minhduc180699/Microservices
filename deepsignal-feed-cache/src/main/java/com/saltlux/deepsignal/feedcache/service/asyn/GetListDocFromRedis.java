package com.saltlux.deepsignal.feedcache.service.asyn;

import com.saltlux.deepsignal.feedcache.model.DocModel;
import com.saltlux.deepsignal.feedcache.redis.RedisConnection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.Callable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetListDocFromRedis implements Callable<List<DocModel>> {

    private List<String> keys;

    private RedisConnection redisConnection;

    @Override
    public List<DocModel> call() throws Exception {
        return redisConnection.getListValueOfDoc(keys);
    }
}
