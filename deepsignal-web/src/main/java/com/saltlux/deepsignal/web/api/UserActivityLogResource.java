package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.UserActivityLog;
import com.saltlux.deepsignal.web.service.IUserActivityLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-activity-log")
@Tag(name = "User Activity Log Management", description = "User Activity Log API")
public class UserActivityLogResource {

    private final IUserActivityLogService userActivityLogService;

    public UserActivityLogResource(IUserActivityLogService userActivityLogService) {
        this.userActivityLogService = userActivityLogService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUserActivityLog(@RequestBody UserActivityLog userActivityLog) {
        return ResponseEntity.ok().body(userActivityLogService.saveUserActivityLogWithBase(userActivityLog));
    }

    @PostMapping("/saveChangeUrl")
    public ResponseEntity<?> saveUserActivityLogChangeUrl(@RequestBody UserActivityLog userActivityLog) {
        userActivityLog.setActivityName(Constants.UserActivities.CHANGE_URL);
        return ResponseEntity.ok().body(userActivityLogService.saveUserActivityLogWithBase(userActivityLog));
    }

    @GetMapping("/getTrainingDocuments/{connectomeId}")
    @Operation(summary = "Get Training Documents", tags = { "User Activity Log Management" })
    public ResponseEntity<?> getTrainingDocuments(
        @PathVariable("connectomeId") String connectomeId,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam("type") String type,
        @RequestParam("from") String dateFrom,
        @RequestParam("to") String dateTo,
        @RequestParam(value = "hour", required = false) Integer hour
    ) {
        try {
            if (Objects.nonNull(keyword) && !keyword.isEmpty()) {
                return ResponseEntity
                    .ok()
                    .body(userActivityLogService.getTrainingDocumentsByKeyword(connectomeId, keyword, dateFrom, dateTo, hour));
            } else {
                return ResponseEntity
                    .ok()
                    .body(userActivityLogService.getTrainingDocumentsByType(connectomeId, type, dateFrom, dateTo, hour));
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
