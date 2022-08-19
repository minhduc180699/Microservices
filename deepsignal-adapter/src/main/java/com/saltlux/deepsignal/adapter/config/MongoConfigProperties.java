package com.saltlux.deepsignal.adapter.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "partner.datasource")
@Configuration
public class MongoConfigProperties {

    private MongoProperties tornadoMongodb = new MongoProperties();
    private MongoProperties serviceMongodb = new MongoProperties();
}
