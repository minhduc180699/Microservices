package com.saltlux.deepsignal.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.domain.ExternalUrlTracking;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.service.IExternalUrlTrackingService;
import com.saltlux.deepsignal.web.service.dto.ExternalUrlTrackingDTO;
import com.saltlux.deepsignal.web.service.dto.UrlTrackingDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Objects;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/countByUrl")
    public ResponseEntity<?> countByUrl(@RequestBody String originalUrl) {
        if (Objects.isNull(originalUrl) || originalUrl.isEmpty()) {
            return ResponseEntity.badRequest().body("URL cannot be null");
        }
        try {
            int clickedCount = iExternalUrlTrackingService.countByOriginalUrl(originalUrl);
            JSONObject responseObj = new JSONObject();
            responseObj.put("clickedCount", clickedCount);
            responseObj.put("url", originalUrl);
            return ResponseEntity.ok().body(responseObj);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Cannot find URL");
        }
    }

    @GetMapping("/countMostClickedUrl")
    public ResponseEntity<?> countMostClickedUrl(@RequestParam("limit") int limit) {
        if (Objects.nonNull(limit) && limit > 0) {
            try {
                List<UrlTrackingDTO> result = iExternalUrlTrackingService.countMostClickedUrl(limit);
                return ResponseEntity.ok().body(result);
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body(ex.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("Limit is not valid");
        }
    }
}
