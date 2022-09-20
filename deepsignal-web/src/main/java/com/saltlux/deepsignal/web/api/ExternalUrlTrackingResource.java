package com.saltlux.deepsignal.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.domain.ExternalUrlTracking;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.service.IExternalUrlTrackingService;
import com.saltlux.deepsignal.web.service.dto.ExternalUrlTrackingDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/url-tracking")
@Tag(name = "External Url Tracking Management", description = "External Url Tracking Management API")
public class ExternalUrlTrackingResource {

    private final ModelMapper modelMapper;

    private final IExternalUrlTrackingService iExternalUrlTrackingService;

    public ExternalUrlTrackingResource(ModelMapper modelMapper, IExternalUrlTrackingService iExternalUrlTrackingService) {
        this.modelMapper = modelMapper;
        this.iExternalUrlTrackingService = iExternalUrlTrackingService;
    }

    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody ExternalUrlTrackingDTO externalUrlTrackingDTO) {
        ExternalUrlTracking externalUrlTracking = modelMapper.map(externalUrlTrackingDTO, ExternalUrlTracking.class);
        User user = new User();
        user.setId(externalUrlTrackingDTO.getUserId());
        Connectome connectome = new Connectome();
        connectome.setConnectomeId(externalUrlTrackingDTO.getConnectomeId());
        externalUrlTracking.setUser(user);
        externalUrlTracking.setConnectome(connectome);
        ExternalUrlTracking result = iExternalUrlTrackingService.save(externalUrlTracking);
        if (Objects.nonNull(result)) {
            return ResponseEntity.ok().body(externalUrlTrackingDTO);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
