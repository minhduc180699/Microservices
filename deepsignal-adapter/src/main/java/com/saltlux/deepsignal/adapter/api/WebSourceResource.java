package com.saltlux.deepsignal.adapter.api;

import com.saltlux.deepsignal.adapter.api.errors.BadRequestAlertException;
import com.saltlux.deepsignal.adapter.config.Constants;
import com.saltlux.deepsignal.adapter.domain.WebSource;
import com.saltlux.deepsignal.adapter.service.dto.WebSourceConditionDTO;
import com.saltlux.deepsignal.adapter.service.impl.WebSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Web Source Management", description = "The web source management API")
public class WebSourceResource {

    private final Logger log = LoggerFactory.getLogger(WebSourceResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WebSourceService webSourceService;

    public WebSourceResource(WebSourceService webSourceService) {
        this.webSourceService = webSourceService;
    }

    @PostMapping("/websource/create")
    @Operation(summary = "Add a new web source", tags = { "Web Source Management" })
    public ResponseEntity<?> createWebSource(@Valid @RequestBody WebSource webSource) throws URISyntaxException {
        log.debug("REST request to save Web source : {} ", webSource);

        if (webSource.getWebSourceId() != null) {
            throw new BadRequestAlertException("A new blog cannot already have an ID", Constants.WEB_SOURCE_ENTITY, "idexists");
        }

        List<WebSource> result = webSourceService.createWebSource(webSource);
        return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/websources")
    @Operation(summary = "Add a new list web source", tags = { "Web Source Management" })
    public ResponseEntity<?> createWebSources(@Valid @RequestBody List<WebSource> webSources) throws URISyntaxException {
        log.debug("REST request to save Web sources : {} ", webSources);

        //        if (webSources.getWebSourceId() == null) {
        //            throw new BadRequestAlertException("A new web source ID cannot null", Constants.WEB_SOURCE_ENTITY, "idnull");
        //        }

        List<WebSource> result = webSourceService.saveAll(webSources);
        return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/websource/add-condition")
    @Operation(summary = "Add a new condition for web source", tags = { "Web Source Management" })
    public ResponseEntity<?> addWebSourceCondition(@Valid @RequestBody WebSourceConditionDTO webSource) throws URISyntaxException {
        log.debug("REST request to save Web source : {} ", webSource);

        WebSourceConditionDTO webSourceConditionDTO = webSourceService.addWebSourceCondition(webSource);
        return new ResponseEntity<>(webSourceConditionDTO, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/websource/update-condition")
    @Operation(summary = "Update a condition for web source", tags = { "Web Source Management" })
    public ResponseEntity<?> updateWebSourceCondition(@Valid @RequestBody WebSourceConditionDTO webSource) throws URISyntaxException {
        log.debug("REST request to save Web source : {} ", webSource);

        List<WebSource> result = webSourceService.updateWebSourceCondition(webSource);
        return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
    }
}
