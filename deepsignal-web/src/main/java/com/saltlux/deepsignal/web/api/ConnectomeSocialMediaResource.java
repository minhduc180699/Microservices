package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.api.errors.BadRequestAlertException;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.ConnectomeSocialMedia;
import com.saltlux.deepsignal.web.security.SecurityUtils;
import com.saltlux.deepsignal.web.service.IConnectomeSocialMediaService;
import com.saltlux.deepsignal.web.service.dto.ConnectomeSocialMediaDTO;
import com.saltlux.deepsignal.web.service.impl.ConnectomeSocialMediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.hibernate.validator.internal.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/connectome-social-media")
@Tag(name = "Connectome Social Management", description = "The connectome social media management API")
public class ConnectomeSocialMediaResource {

    private final Logger log = LoggerFactory.getLogger(ConnectomeSocialMediaResource.class);

    private static final String ENTITY_NAME = "connectome_social_media";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IConnectomeSocialMediaService connectomeSocialMediaService;

    public ConnectomeSocialMediaResource(ConnectomeSocialMediaService connectomeSocialMediaService) {
        this.connectomeSocialMediaService = connectomeSocialMediaService;
    }

    @PostMapping("/create")
    @Operation(summary = "Add a connectome social media", tags = { "Connectome Social Management" })
    public ResponseEntity<?> createConnectomeSocialMedia(@Valid @RequestBody ConnectomeSocialMediaDTO socialMediaDTO)
        throws URISyntaxException {
        log.debug("REST request to save SNS account : {} ", socialMediaDTO);

        if (socialMediaDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new connectome social media cannot already have an ID",
                ENTITY_NAME,
                Constants.ErrorKeys.IDEXISTS.label
            );
        }
        if (StringHelper.isNullOrEmptyString(socialMediaDTO.getUser().getId())) {
            throw new BadRequestAlertException("User id cannot be empty", ENTITY_NAME, Constants.ErrorKeys.IDUSERNULL.label);
        }
        if (!socialMediaDTO.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse(""))) {
            return new ResponseEntity<>("error.http.403", HttpStatus.FORBIDDEN);
        }

        ConnectomeSocialMedia result = connectomeSocialMediaService.createConnectomeSocialMedial(socialMediaDTO);
        return ResponseEntity
            .created(new URI("/api/connectome-social-media/create/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
