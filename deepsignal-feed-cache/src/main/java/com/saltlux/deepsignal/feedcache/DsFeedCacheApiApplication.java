package com.saltlux.deepsignal.feedcache;

import com.saltlux.deepsignal.feedcache.runable.FeedSummaryThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@RefreshScope
@EnableFeignClients
public class DsFeedCacheApiApplication {
    @Autowired
    private FeedSummaryThread feedSummaryThread;
    @Bean
    public void runThread(){
        Thread t1 = new Thread(this.feedSummaryThread);
        t1.start();
    }
    public static void main(String[] args) {
        SpringApplication.run(DsFeedCacheApiApplication.class, args);
    }
}
@Configuration
class RestTemplateConfig {
    // Create a bean for restTemplate to call services
    @Bean
//    @LoadBalanced // Load balance between service instances running at different ports.
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
