package com.saltlux.deepsignal.feedcache.service.asyn;

import com.saltlux.deepsignal.feedcache.model.DocContentModel;
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
public class GetListDocContentFromRedis implements Callable<List<DocContentModel>> {

    private List<String> keys;

    private RedisConnection redisConnection;

    @Override
    public List<DocContentModel> call() throws Exception {
        return redisConnection.getListValueOfDocContent(keys);
    }
}
