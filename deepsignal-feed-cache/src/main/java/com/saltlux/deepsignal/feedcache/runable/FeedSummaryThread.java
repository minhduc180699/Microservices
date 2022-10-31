package com.saltlux.deepsignal.feedcache.runable;

import com.google.common.reflect.TypeToken;
import com.saltlux.deepsignal.feedcache.config.Appconfig;
import com.saltlux.deepsignal.feedcache.constant.KafkaConstant;
import com.saltlux.deepsignal.feedcache.kafka.KafkaConsumer;
import com.saltlux.deepsignal.feedcache.model.DocCreateModel;
import com.saltlux.deepsignal.feedcache.model.DocDataModel;
import com.saltlux.deepsignal.feedcache.model.FeedMetaSearchModel;
import com.saltlux.deepsignal.feedcache.service.IFeedService;
import com.saltlux.deepsignal.feedcache.utils.GUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
@Component
public class FeedSummaryThread implements Runnable{
    private Logger logger = LoggerFactory.getLogger(FeedSummaryThread.class);
    @Autowired
    private IFeedService feedService;
    @Autowired
    private Appconfig appconfig;

    @Override
    public void run() {
        KafkaConsumer consumer = new KafkaConsumer("192.168.2.141:9092,192.168.2.142:9092,192.168.2.143:9092", "FeedCache1", KafkaConstant.SUMMARY_FEED_TOPIC);
        while (true) {
            //TODO: Handler push a array sub request
            //TODO: If not contain content push RC
            try {
                LinkedList<DocDataModel> docDataModels = new LinkedList<>();
                ConsumerRecords<String, String> records = consumer.getConsumer().poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    List<DocCreateModel> docDataModelList = GUtil.gson.fromJson(record.value(), new TypeToken<List<FeedMetaSearchModel>>() {
                    }.getType());
                    for (DocCreateModel docDataModel : docDataModelList) {
                        feedService.createFeed(docDataModel);
                    }
                }
                Thread.sleep(1000);
            } catch (Exception e) {

                logger.error("[FeedSummaryThread] error: " + e.getMessage(), e);
            }
        }
    }
}
