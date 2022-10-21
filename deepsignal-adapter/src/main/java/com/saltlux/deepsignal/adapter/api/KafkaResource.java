package com.saltlux.deepsignal.adapter.api;

import com.saltlux.deepsignal.adapter.config.ApplicationProperties;
import com.saltlux.deepsignal.adapter.service.KafkaService;
import com.saltlux.deepsignal.adapter.service.dto.FileInfoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/kafka")
@Tag(name = "Kafka Management", description = "The kafka management API")
public class KafkaResource {

    private final KafkaService kafkaService;

    private final ApplicationProperties properties;

    @Value("${application.kafka-cfg.messages-per-request:dev}")
    private String msgPerRequest;

    public KafkaResource(KafkaService kafkaService, ApplicationProperties properties) {
        this.kafkaService = kafkaService;
        this.properties = properties;
    }

    @PostMapping(value = "/publish")
    @Operation(summary = "Send a message to Kafka topic", tags = { "Kafka Management" })
    public ResponseEntity<?> sendMessageToKafkaTopic(@Valid @RequestBody FileInfoDTO[] fileInfoDTO) {
        kafkaService.sendMessage(fileInfoDTO);
        return new ResponseEntity<>(fileInfoDTO, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(value = "/properties")
    @Operation(summary = "Get Kafka info", tags = { "Kafka Management" })
    public ResponseEntity<?> getProperties() {
        return new ResponseEntity<>(properties.getKafkaCfg(), new HttpHeaders(), HttpStatus.OK);
    }


    @GetMapping(value = "/msgPerRequest")
    @Operation(summary = "Get Kafka Message Per Request", tags = { "Kafka Management" })
    public ResponseEntity<?> getMsgPerRequest() {
        return new ResponseEntity<>(msgPerRequest, new HttpHeaders(), HttpStatus.OK);
    }
}
