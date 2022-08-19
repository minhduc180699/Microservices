package com.saltlux.deepsignal.adapter.service;

import com.saltlux.deepsignal.adapter.service.dto.FileInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafkaService {

    private final Logger log = LoggerFactory.getLogger(KafkaService.class);

    private final KafkaTemplate<String, FileInfoDTO[]> kafkaTemplate;
    private static final String TOPIC = "ds_file_upload";

    public KafkaService(KafkaTemplate<String, FileInfoDTO[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(FileInfoDTO[] fileInfo) {
        log.info(String.format("$$$$ => Upload file to Kafka processing, ", fileInfo.length));

        ListenableFuture<SendResult<String, FileInfoDTO[]>> future = this.kafkaTemplate.send(TOPIC, fileInfo);
        future.addCallback(
            new ListenableFutureCallback<SendResult<String, FileInfoDTO[]>>() {
                @Override
                public void onFailure(Throwable ex) {
                    log.info("Unable to send message=[ {} ] due to : {}", fileInfo, ex.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, FileInfoDTO[]> result) {
                    log.info("Sent message=[ {} ] with offset=[ {} ]", fileInfo, result.getRecordMetadata().offset());
                }
            }
        );
    }

    @KafkaListener(topics = TOPIC, groupId = "DeepSignal-Kafka")
    public void consume(String message) {
        //        log.info(String.format("$$$$ => Consumed message: %s", message));
    }
}
