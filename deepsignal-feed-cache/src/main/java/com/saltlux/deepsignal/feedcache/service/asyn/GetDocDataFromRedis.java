package com.saltlux.deepsignal.feedcache.service.asyn;

import com.saltlux.deepsignal.feedcache.model.DocContentModel;
import com.saltlux.deepsignal.feedcache.model.DocDataModel;
import com.saltlux.deepsignal.feedcache.model.DocModel;
import com.saltlux.deepsignal.feedcache.redis.RedisConnection;
import com.saltlux.deepsignal.feedcache.utils.GUtil;
import io.lettuce.core.RedisFuture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetDocDataFromRedis implements Callable<DocDataModel> {

    private String connectomeId;
    private String docId;
    private RedisConnection redisConnection;

    private static final Logger logger = LoggerFactory.getLogger(GetDocDataFromES.class);

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public DocDataModel call() throws Exception {
//        GetDocFromRedis docFromRedis = new GetDocFromRedis(connectomeId + "_" + docId, redisConnection);
//        GetDocContentFromRedis docContentFromRedis = new GetDocContentFromRedis(docId, redisConnection);
//        DocModel docModel = executorService.submit(docFromRedis).get(5, TimeUnit.SECONDS);
//        DocContentModel docContentModel = executorService.submit(docContentFromRedis).get(5, TimeUnit.SECONDS);
//        DocDataModel docDataModel = new DocDataModel();
//        try {
//            docDataModel.buildFeedDataModel(docModel,docContentModel);
//        }catch (NullPointerException e){
//            logger.error(String.format("connectomeId: %s, docId: %s, document from Redis null , error: %s", connectomeId, docId , e.getMessage()));
//            docDataModel.setRequestId(null);
//            docDataModel.setCreated_by(null);
//            docDataModel.setCollector(null);
//        }
//        return null;

        RedisFuture<String> docFromRedisFuture = redisConnection.getDocAsync(connectomeId + "_" + docId);
        RedisFuture<String> docContentFromRedisFuture = redisConnection.getDocContentAsync(docId);

        DocModel docModel = GUtil.gson.fromJson(docFromRedisFuture.get(3,TimeUnit.SECONDS), DocModel.class);
        DocContentModel docContentModel = GUtil.gson.fromJson(docContentFromRedisFuture.get(3,TimeUnit.SECONDS), DocContentModel.class);

        DocDataModel docDataModel = new DocDataModel();

        try {
            docDataModel.buildFeedDataModel(docModel,docContentModel);
        }catch (NullPointerException e){
            logger.error(String.format("connectomeId: %s, docId: %s, document from Redis null , error: %s", connectomeId, docId , e.getMessage()));
            docDataModel.setRequestId(null);
            docDataModel.setCreated_by(null);
            docDataModel.setCollector(null);
        }
        return docDataModel;
    }
}
