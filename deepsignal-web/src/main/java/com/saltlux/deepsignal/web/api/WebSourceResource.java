package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.api.errors.BadRequestAlertException;
import com.saltlux.deepsignal.web.domain.WebSource;
import com.saltlux.deepsignal.web.service.IWebSourceService;
import com.saltlux.deepsignal.web.service.dto.WebSourceConditionDTO;
import com.saltlux.deepsignal.web.service.impl.WebSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
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
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/websource")
@Tag(name = "Web Source Management", description = "The web source management API")
public class WebSourceResource {

    private final Logger log = LoggerFactory.getLogger(WebSourceResource.class);

    private static final String ENTITY_NAME = "websource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IWebSourceService webSourceService;

    public WebSourceResource(WebSourceService webSourceService) {
        this.webSourceService = webSourceService;
    }

    @PostMapping("/create")
    @Operation(summary = "Creates a new web source", tags = { "Web Source Management" })
    public ResponseEntity<?> createWebSource(@Valid @RequestBody WebSource webSource) throws URISyntaxException {
        log.debug("REST request to save Web source : {} ", webSource);

        if (webSource.getWebSourceId() != null) {
            throw new BadRequestAlertException("A new web source cannot already have an ID", ENTITY_NAME, "idexists");
        }

        WebSource result = webSourceService.createWebSource(webSource);
        return ResponseEntity
            .created(new URI("/api/websource/create" + result.getWebSourceId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getWebSourceId()))
            .body(result);
    }

    @PostMapping("/add-condition")
    @Operation(summary = "Add a new condition for web source", tags = { "Web Source Management" })
    public ResponseEntity<?> addWebSourceCondition(@Valid @RequestBody WebSourceConditionDTO webSource) throws URISyntaxException {
        log.debug("REST request to save Web source : {} ", webSource);

        List<WebSource> result = webSourceService.addWebSourceCondition(webSource);
        return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
    }
}
