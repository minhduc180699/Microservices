package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.domain.ExternalUrl;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.domain.UserUrlTracking;
import com.saltlux.deepsignal.web.domain.compositekey.UserUrlTrackingId;
import com.saltlux.deepsignal.web.service.IExternalUrlService;
import com.saltlux.deepsignal.web.service.IUserUrlTrackingService;
import com.saltlux.deepsignal.web.service.dto.ExternalUrlDTO;
import com.saltlux.deepsignal.web.service.dto.UrlTrackingDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external-url")
@Tag(name = "External Url Management", description = "External Url Management API")
public class ExternalUrlResource {

    private final ModelMapper modelMapper;

    private final IExternalUrlService iExternalUrlService;

    private final IUserUrlTrackingService iUserUrlTrackingService;

    public ExternalUrlResource(
        ModelMapper modelMapper,
        IExternalUrlService iExternalUrlService,
        IUserUrlTrackingService iUserUrlTrackingService
    ) {
        this.modelMapper = modelMapper;
        this.iExternalUrlService = iExternalUrlService;
        this.iUserUrlTrackingService = iUserUrlTrackingService;
    }

    @PostMapping("{userId}")
    public ResponseEntity<?> save(@PathVariable("userId") String userId, @RequestBody ExternalUrlDTO externalUrlDTO) {
        Optional<ExternalUrl> externalUrlOptional = iExternalUrlService.findByOriginalUrl(externalUrlDTO.getOriginalUrl());
        if (externalUrlOptional.isPresent()) {
            Optional<UserUrlTracking> userUrlTrackingOptional = iUserUrlTrackingService.findById(
                new UserUrlTrackingId(userId, externalUrlOptional.get().getId())
            );
            if (userUrlTrackingOptional.isPresent()) {
                userUrlTrackingOptional.get().setClick(userUrlTrackingOptional.get().getClick() + 1);
                iUserUrlTrackingService.save(userUrlTrackingOptional.get());
                return ResponseEntity.ok().body(externalUrlOptional.get());
            } else {
                UserUrlTracking userUrlTracking = new UserUrlTracking(userId, externalUrlOptional.get().getId(), 1);
                iUserUrlTrackingService.save(userUrlTracking);
                return ResponseEntity.ok().body(externalUrlOptional.get());
            }
        } else {
            ExternalUrl url = modelMapper.map(externalUrlDTO, ExternalUrl.class);
            String shortUrl = iExternalUrlService.createShortUrl(externalUrlDTO.getOriginalUrl());
            url.setShortUrl(shortUrl);
            ExternalUrl externalUrl = iExternalUrlService.save(url);
            UserUrlTracking userUrlTracking = iUserUrlTrackingService.save(new UserUrlTracking(userId, externalUrl.getId(), 1));
            if (Objects.nonNull(externalUrl) && Objects.nonNull(userUrlTracking)) {
                return ResponseEntity.ok().body(externalUrl);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        }
    }

    @GetMapping("/deepsignal/{shortUrl}")
    public ResponseEntity<?> redirect(HttpServletResponse response, @PathVariable("shortUrl") String shortUrl)
        throws IOException, URISyntaxException {
        Optional<ExternalUrl> externalUrl = iExternalUrlService.findByShortUrl(shortUrl);
        if (externalUrl.isPresent()) {
            return ResponseEntity.ok(externalUrl.get().getOriginalUrl());
            //            response.sendRedirect(externalUrl.get().getOriginalUrl());
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/countByUrl")
    public ResponseEntity<?> countByUrl(@RequestBody String originalUrl) {
        if (Objects.isNull(originalUrl) || originalUrl.isEmpty()) {
            return ResponseEntity.badRequest().body("URL cannot be null");
        }
        try {
            int clickedCount = iExternalUrlService.countByOriginalUrl(originalUrl);
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
                List<UrlTrackingDTO> result = iExternalUrlService.countMostClickedUrl(limit);
                return ResponseEntity.ok().body(result);
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body(ex.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("Limit is not valid");
        }
    }
}
