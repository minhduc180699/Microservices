package com.saltlux.deepsignal.adapter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.saltlux.deepsignal.adapter.repository.dstornado", mongoTemplateRef = "mongoTornadoTemplate")
public class MongoConfigTonado {}
