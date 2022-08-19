package com.saltlux.deepsignal.adapter.api;

import com.saltlux.deepsignal.adapter.service.KafkaService;
import com.saltlux.deepsignal.adapter.service.dto.FileInfoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
@Tag(name = "Kafka Management", description = "The kafka management API")
public class KafkaResource {

    private final KafkaService kafkaService;

    public KafkaResource(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @PostMapping(value = "/publish")
    @Operation(summary = "Send a message to Kafka topic", tags = { "Kafka Management" })
    public ResponseEntity<?> sendMessageToKafkaTopic(@Valid @RequestBody FileInfoDTO[] fileInfoDTO) {
        kafkaService.sendMessage(fileInfoDTO);
        return new ResponseEntity<>(fileInfoDTO, new HttpHeaders(), HttpStatus.OK);
    }
}
