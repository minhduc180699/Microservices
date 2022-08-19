package com.saltlux.deepsignal.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saltlux.deepsignal.web.api.errors.BadRequestAlertException;
import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.exception.AccountAlreadyNotExistException;
import com.saltlux.deepsignal.web.exception.AccountTemporaryException;
import com.saltlux.deepsignal.web.security.SecurityUtils;
import com.saltlux.deepsignal.web.service.IConnectomeService;
import com.saltlux.deepsignal.web.service.dto.ApiResponse;
import com.saltlux.deepsignal.web.service.dto.ConnectomeDTO;
import com.saltlux.deepsignal.web.service.dto.ConnectomeRes;
import com.saltlux.deepsignal.web.service.impl.ConnectomeService;
import com.saltlux.deepsignal.web.util.ConnectAdapterApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.internal.util.StringHelper;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing the Connectome.
 */
@RestController
@RequestMapping("/api/toolbox")
@Tag(name = "Toolbox Management", description = "The toolbox management API")
public class ToolboxResource {

    private final Logger log = LoggerFactory.getLogger(ToolboxResource.class);

    private static final String ENTITY_NAME = "toolbox";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    public ToolboxResource() {}

    @PostMapping(value = "/{lang}/keywords", consumes = "application/json", produces = "application/json")
    @Operation(summary = "post request keywords analysis ", tags = { "Toolbox Management" })
    public ResponseEntity<?> postKeywordsAnalysis(
        @PathVariable("lang") String lang,
        @Valid @org.springframework.web.bind.annotation.RequestBody KeywordsParams keywordsParams
    ) {
        if (Objects.isNull(keywordsParams)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (lang == null || lang.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            // InetSocketAddress host = InetSocketAddress.createUnresolved("192.168.10.18", 5555);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject paramsJsonObject = new JSONObject();
            paramsJsonObject.put("request_id", keywordsParams.request_id);
            paramsJsonObject.put("text", keywordsParams.text);

            headers.setContentLength(paramsJsonObject.toString().length());

            System.out.println(headers);
            System.out.println(applicationProperties.getExternalApi().getDeepsignalToolbox() + "/" + lang + "/keywords/");

            HttpEntity<String> request = new HttpEntity<String>(paramsJsonObject.toString(), headers);

            Object result = restTemplate.postForObject(
                applicationProperties.getExternalApi().getDeepsignalToolbox() + "/" + lang + "/keywords/",
                request,
                Object.class
            );
            return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/{lang}/entity_disambiguation", consumes = "application/json", produces = "application/json")
    @Operation(summary = "post request entity disambiguation analysis ", tags = { "Toolbox Management" })
    public ResponseEntity<?> postEntityDisambiguationAnalysis(
        @PathVariable("lang") String lang,
        @Valid @org.springframework.web.bind.annotation.RequestBody KeywordsParams keywordsParams
    ) {
        if (Objects.isNull(keywordsParams)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (lang == null || lang.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject paramsJsonObject = new JSONObject();
            paramsJsonObject.put("request_id", keywordsParams.request_id);
            paramsJsonObject.put("text", keywordsParams.text);

            HttpEntity<String> request = new HttpEntity<String>(paramsJsonObject.toString(), headers);

            Object result = restTemplate.postForObject(
                applicationProperties.getExternalApi().getDeepsignalToolbox() + "/" + lang + "/entity_disambiguation/debug/",
                request,
                Object.class
            );
            return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class KeywordsParams {

    public String request_id;

    public String text;
}
