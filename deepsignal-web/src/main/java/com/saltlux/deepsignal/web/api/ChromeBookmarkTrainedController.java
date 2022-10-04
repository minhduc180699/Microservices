package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.domain.ChromeBookmarkTrained;
import com.saltlux.deepsignal.web.domain.UserSetting;
import com.saltlux.deepsignal.web.service.IChromeBookmarkTrainedService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chrome-bookmark")
@Tag(name = "ChromeBookmarkTrained Management", description = "The ChromeBookmarkTrained management API")
public class ChromeBookmarkTrainedController {

    private final IChromeBookmarkTrainedService chromeBookmarkTrainedService;

    public ChromeBookmarkTrainedController(IChromeBookmarkTrainedService chromeBookmarkTrainedService) {
        this.chromeBookmarkTrainedService = chromeBookmarkTrainedService;
    }

    @GetMapping("/{connectomeId}")
    public ResponseEntity<?> getByUserId(@PathVariable("connectomeId") String connectomeId) {
        if (StringUtils.isEmpty(connectomeId)) {
            return ResponseEntity.badRequest().body("connectomeId is null");
        }
        return ResponseEntity.ok().body(chromeBookmarkTrainedService.findAllByConnectomeId(connectomeId));
    }

    @PostMapping("/{connectomeId}")
    public ResponseEntity<?> save(
        @RequestBody List<ChromeBookmarkTrained> lstBookmarkTrained,
        @PathVariable("connectomeId") String connectomeId
    ) {
        try {
            chromeBookmarkTrainedService.save(lstBookmarkTrained, connectomeId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Save successfully!");
    }
}
