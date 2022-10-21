package com.saltlux.deepsignal.feedcache.kafka;

import com.saltlux.deepsignal.feedcache.config.Appconfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.Future;
@Component
public class Producer {
    private static Producer instance = null;
    private org.apache.kafka.clients.producer.KafkaProducer<String, String> producer;

    public Producer(Appconfig appconfig) {
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, appconfig.getKafka().getBrokers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, Integer.toString(1));
        //for sharding
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, org.apache.kafka.clients.producer.RoundRobinPartitioner.class.getName());

        producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);
        this.producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);

    }

    public void send(String topic, String message) {
        ProducerRecord<String, String> keyMessage = new ProducerRecord<>(topic, null, message/*for sharding*/);
        Future<RecordMetadata> meta = producer.send(keyMessage);
        //TODO: tam thoi
        producer.flush();
    }

    public org.apache.kafka.clients.producer.KafkaProducer<String, String> getProducer() {
        return producer;
    }

    public void setProducer(org.apache.kafka.clients.producer.KafkaProducer<String, String> producer) {
        this.producer = producer;
    }
}