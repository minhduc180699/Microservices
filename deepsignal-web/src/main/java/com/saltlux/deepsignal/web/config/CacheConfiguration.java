package com.saltlux.deepsignal.web.config;

import com.saltlux.deepsignal.web.domain.ExternalUrl;
import com.saltlux.deepsignal.web.domain.UserActivityLog;
import com.saltlux.deepsignal.web.domain.UserUrlTracking;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private RedissonClient redissonClient;

    @Bean
    public javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration(JHipsterProperties jHipsterProperties) {
        MutableConfiguration<Object, Object> jcacheConfig = new MutableConfiguration<>();

        URI redisUri = URI.create(jHipsterProperties.getCache().getRedis().getServer()[0]);

        Config config = new Config();
        if (jHipsterProperties.getCache().getRedis().isCluster()) {
            ClusterServersConfig clusterServersConfig = config
                .useClusterServers()
                .setMasterConnectionPoolSize(jHipsterProperties.getCache().getRedis().getConnectionPoolSize())
                .setMasterConnectionMinimumIdleSize(jHipsterProperties.getCache().getRedis().getConnectionMinimumIdleSize())
                .setSubscriptionConnectionPoolSize(jHipsterProperties.getCache().getRedis().getSubscriptionConnectionPoolSize())
                .addNodeAddress(jHipsterProperties.getCache().getRedis().getServer());

            if (redisUri.getUserInfo() != null) {
                clusterServersConfig.setPassword(redisUri.getUserInfo().substring(redisUri.getUserInfo().indexOf(':') + 1));
            }
        } else {
            SingleServerConfig singleServerConfig = config
                .useSingleServer()
                .setConnectionPoolSize(jHipsterProperties.getCache().getRedis().getConnectionPoolSize())
                .setConnectionMinimumIdleSize(jHipsterProperties.getCache().getRedis().getConnectionMinimumIdleSize())
                .setSubscriptionConnectionPoolSize(jHipsterProperties.getCache().getRedis().getSubscriptionConnectionPoolSize())
                .setAddress(jHipsterProperties.getCache().getRedis().getServer()[0]);

            if (redisUri.getUserInfo() != null) {
                singleServerConfig.setPassword(redisUri.getUserInfo().substring(redisUri.getUserInfo().indexOf(':') + 1));
            }
        }
        jcacheConfig.setStatisticsEnabled(true);
        jcacheConfig.setExpiryPolicyFactory(
            CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, jHipsterProperties.getCache().getRedis().getExpiration()))
        );
        redissonClient = Redisson.create(config);
        return RedissonConfiguration.fromInstance(redissonClient, jcacheConfig);
    }

    @Bean
    public RMapCache<String, String> mapSmsCode() {
        return redissonClient.getMapCache("mapSmsCode");
    }

    @Bean
    public RMapCache<String, String> mapToken() {
        return redissonClient.getMapCache("mapToken");
    }

    @Bean
    public RMapCache<String, String> mapEmailCode() {
        return redissonClient.getMapCache("mapEmailCode");
    }

    @Bean
    public RBlockingQueue<UserActivityLog> queueUserActivity() {
        return redissonClient.getBlockingQueue("queueUserActivity");
    }

    @Bean
    public RMapCache<String, List<String>> mapNotification() {
        return redissonClient.getMapCache("mapNotification");
    }

    @Bean
    public RMapCache<String, List<String>> mapUpdateFeed() {
        return redissonClient.getMapCache("mapUpdateFeed");
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(CacheManager cm) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cm);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer(javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration) {
        return cm -> {
            createCache(cm, com.saltlux.deepsignal.web.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.User.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.Authority.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.User.class.getName() + ".purposes", jcacheConfiguration);
            // jhipster-needle-redis-add-entry
            createCache(cm, com.saltlux.deepsignal.web.domain.Category.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.CodeLanguage.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.CodeServiceCrawlerType.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.CodeServiceType.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.Comment.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.Connectome.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.ConnectomeSocialMedia.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.ConnectomeSocialMedia.class.getName() + ".connectomes", jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.Faqs.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.FavoriteKeyword.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.FileInfo.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.FileInfo.class.getName() + ".connectomes", jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.InquiryAnswer.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.InquiryAnswerEmail.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.InquiryQuestion.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.Interaction.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.InteractionUser.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.LinkShare.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.Notification.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.NotificationReceiver.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.NotificationType.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.Purpose.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.RecentSearch.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.Heatmap.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.SocialNetwork.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.UserActivityLog.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.WebSource.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.WebSource.class.getName() + ".connectomes", jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.WebSourceTemplate.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.WebSourceTemplate.class.getName() + ".connectomes", jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.SignalKeywords.class.getName(), jcacheConfiguration);
            createCache(cm, com.saltlux.deepsignal.web.domain.UserSetting.class.getName(), jcacheConfiguration);
            createCache(cm, ExternalUrl.class.getName(), jcacheConfiguration);
            createCache(cm, UserUrlTracking.class.getName(), jcacheConfiguration);
        };
    }

    private void createCache(
        javax.cache.CacheManager cm,
        String cacheName,
        javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration
    ) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
