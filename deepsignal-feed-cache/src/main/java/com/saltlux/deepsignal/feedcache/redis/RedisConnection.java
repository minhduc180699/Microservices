package com.saltlux.deepsignal.feedcache.redis;


import com.google.gson.JsonObject;
import com.saltlux.deepsignal.feedcache.config.Appconfig;
import com.saltlux.deepsignal.feedcache.model.FeedContentModel;
import com.saltlux.deepsignal.feedcache.model.FeedModel;
import com.saltlux.deepsignal.feedcache.utils.GUtil;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RedisConnection {
    private Logger logger = LoggerFactory.getLogger(RedisConnection.class);
    @Autowired
    private Appconfig appconfig;
    static private volatile boolean _bStop = false;
    private static RedisConnection instance = null;
    public StatefulRedisClusterConnection<String, String> connection;
    public RedisAdvancedClusterCommands<String, String> syncCmd = null;
    public RedisClusterAsyncCommands<String, String> asSyncCmd = null;
    List<RedisURI> cluster = new ArrayList<>();

    public RedisConnection(Appconfig appconfig) {
        try {
            this.appconfig = appconfig;
            String[] hostAndPortList = appconfig.getRedis().getUri().split(",");
            for (String hostAndPort : hostAndPortList){
                String[] hostAndPortArray = hostAndPort.split(":");
                RedisURI node = RedisURI.create(hostAndPortArray[0], Integer.parseInt(hostAndPortArray[1]));
                node.setPassword(appconfig.getRedis().getPassword());
                cluster.add(node);
            }
            RedisClusterClient redisClient = RedisClusterClient.create(cluster);
            connection = redisClient.connect();
            syncCmd = connection.sync();
            asSyncCmd = connection.async();
        } catch (Exception e) {

        }
    }

    public void reconnectRedis() {
        if (connection.isOpen()) {
            connection.close();
        }
        if (syncCmd.isOpen()) {
            syncCmd.quit();
        }
        if (asSyncCmd.isOpen()) {
            asSyncCmd.quit();
        }
        RedisClusterClient redisClient = RedisClusterClient.create(cluster);
        connection = redisClient.connect();
        syncCmd = connection.sync();
        asSyncCmd = connection.async();
    }

    public FeedModel getValueOfFeed(String key){
        try{
            String value = syncCmd.get(appconfig.getRedis().getFeedPrefixCache() + key);
            if (value == null || value.isEmpty()) return null;
            return GUtil.gson.fromJson(value, FeedModel.class);
        }catch (Exception e){
            logger.error("[getValueOfFeed] Redis error ", e);
            return null;
        }
    }
    public JsonObject getValueOfFeedTypeJsonObject(String key){
        try{
            String value = syncCmd.get(appconfig.getRedis().getFeedPrefixCache() + key);
            if (value == null || value.isEmpty()) return null;
            return GUtil.gson.fromJson(value, JsonObject.class);
        }catch (Exception e){
            logger.error("[getValueOfFeedTypeJsonObject] Redis error ", e);
            return null;
        }
    }
    public void saveValueToFeedAsSync(String key, String value){
        try{
            String redisKey = appconfig.getRedis().getFeedPrefixCache() + key;
            asSyncCmd.setex(redisKey, appconfig.getRedis().getExpiredTime(), value);
        }catch (Exception e){
            logger.error("[saveValueToFeedAsSync] Redis error ", e);
        }
    }
    public FeedContentModel getValueOfFeedContent(String key){
        try{
            String value = syncCmd.get(appconfig.getRedis().getFeedContentPrefixCache() + key);
            if (value == null || value.isEmpty()) return null;
            return GUtil.gson.fromJson(value, FeedContentModel.class);
        }catch (Exception e){
            logger.error("[getValueOfFeedContent] Redis error ", e);
            return null;
        }
    }
    public void saveValueToFeedContentAsSync(String key, String value){
        try{
            String redisKey = appconfig.getRedis().getFeedContentPrefixCache() + key;
            asSyncCmd.setex(redisKey, appconfig.getRedis().getExpiredTime(), value);
        }catch (Exception e){
            logger.error("[saveValueToFeedContentAsSync] Redis error ", e);
        }
    }
}
