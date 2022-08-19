package com.saltlux.deepsignal.web.api;

import static com.saltlux.deepsignal.web.util.TemplateConstant.WEBSOCKET_URL_NOTIFICATION;
import static com.saltlux.deepsignal.web.util.TemplateConstant.WEBSOCKET_URL_UPDATE_FEED;

import com.saltlux.deepsignal.web.service.dto.CategoryDTO;
import com.saltlux.deepsignal.web.util.ObjectMapperUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.redisson.api.RMapCache;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cache")
@Tag(name = "Cache Management", description = "The Cache management API")
public class CacheResource {

    private final RMapCache<String, List<String>> mapUpdateFeed;
    private final RMapCache<String, List<String>> mapNotification;

    public CacheResource(RMapCache<String, List<String>> mapUpdateFeed, RMapCache<String, List<String>> mapNotification) {
        this.mapUpdateFeed = mapUpdateFeed;
        this.mapNotification = mapNotification;
    }

    @GetMapping("/checkUpdateFeed/{userId}")
    @Operation(summary = "Check cache saved by consumer rabbitmq for feed update", tags = { "Cache Management" })
    public ResponseEntity<?> checkUpdateFeed(@PathVariable String userId) {
        List<String> ids = mapUpdateFeed.get(WEBSOCKET_URL_UPDATE_FEED);
        if (null != ids && ids.size() > 0) {
            Set<String> idSet = new HashSet<>(ids);
            if (idSet.contains(userId)) {
                idSet.remove(userId);
                ids.clear();
                ids.addAll(idSet);
                mapUpdateFeed.put(WEBSOCKET_URL_UPDATE_FEED, ids);
                return ResponseEntity.ok().body(true);
            }
        }
        return ResponseEntity.ok().body(false);
    }

    @GetMapping("/checkNotification/{userId}")
    @Operation(summary = "Check cache saved by consumer rabbitmq for notification", tags = { "Cache Management" })
    public ResponseEntity<?> checkNotification(@PathVariable String userId) {
        List<String> ids = mapNotification.get(WEBSOCKET_URL_NOTIFICATION);
        if (null != ids && ids.size() > 0) {
            Set<String> idSet = new HashSet<>(ids);
            if (idSet.contains(userId)) {
                idSet.remove(userId);
                ids.clear();
                ids.addAll(idSet);
                mapNotification.put(WEBSOCKET_URL_NOTIFICATION, ids);
                return ResponseEntity.ok().body(true);
            }
        }
        return ResponseEntity.ok().body(false);
    }
}
