package com.saltlux.deepsignal.feedcache.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumer {
    private org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer;

    public KafkaConsumer(String bootstrapServer, String groupId, String topic) {

        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "20000");
        props.setProperty(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "5000");
        props.setProperty(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "1000000");
        props.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));
    }

    public org.apache.kafka.clients.consumer.KafkaConsumer<String, String> getConsumer() {
        return consumer;
    }
}
