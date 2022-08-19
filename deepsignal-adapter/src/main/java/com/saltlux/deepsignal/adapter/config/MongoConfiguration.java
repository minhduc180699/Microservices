package com.saltlux.deepsignal.adapter.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MongoConfigProperties.class)
public class MongoConfiguration {

    private final MongoConfigProperties mongoConfigProperties;

    @Bean("mongoTornadoTemplate")
    public MongoTemplate mongoTornadoTemplate() {
        MongoTemplate mongoTemplate = new MongoTemplate(
            mongo(this.mongoConfigProperties.getTornadoMongodb()),
            this.mongoConfigProperties.getTornadoMongodb().getDatabase()
        );
        return mongoTemplate;
    }

    @Bean("mongoServiceTemplate")
    @Primary
    public MongoTemplate mongoServiceTemplate() {
        MongoTemplate mongoTemplate = new MongoTemplate(
            mongo(this.mongoConfigProperties.getServiceMongodb()),
            this.mongoConfigProperties.getServiceMongodb().getDatabase()
        );
        ((MappingMongoConverter) mongoTemplate.getConverter()).setTypeMapper(new DefaultMongoTypeMapper(null));
        return mongoTemplate;
    }

    //    @Bean
    //    public MongoClient mongoDbFactoryReplica(MongoProperties mongoProperties) {
    //        String dbName = mongoProperties.getDatabase();
    //        String userName = mongoProperties.getUsername();
    //        char[] password = mongoProperties.getPassword();
    //        String addressServer = mongoProperties.getHost();
    //        List<ServerAddress> listAddressServer = new ArrayList<>();
    //        String[] addressArr = addressServer.split(",");
    //        for (String adr : addressArr) {
    //            String[] strArr = adr.split(":");
    //            listAddressServer.add(new ServerAddress(strArr[0], Integer.parseInt(strArr[1])));
    //        }
    //
    //        MongoCredential credential = MongoCredential.createScramSha1Credential(userName, dbName, password);
    //        MongoClientSettings settings = MongoClientSettings.builder()
    //            .credential(credential).readPreference(ReadPreference.primaryPreferred())
    //            .applyToClusterSettings(builder ->
    //                builder.hosts(listAddressServer))
    //            .build();
    //
    //        MongoClient mongoClient = MongoClients.create(settings);
    //            return mongoClient;
    //        }

    public MongoClient mongo(MongoProperties mongoProperties) {
        ConnectionString connectionString = new ConnectionString(mongoProperties.getUri());
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString).build();

        return MongoClients.create(mongoClientSettings);
    }
}
