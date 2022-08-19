package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.api.websocket.dto.NotificationDTO;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.service.INotificationService;
import io.netty.util.internal.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@Tag(name = "Notification Management", description = "The Notification management API")
public class NotificationResource {

    private final INotificationService notificationService;

    public NotificationResource(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/paging")
    @Operation(summary = "Get All Notification By User And Paging", tags = { "Notification Management" })
    public ResponseEntity<?> getAllNotification(Pageable pageable, @RequestParam(required = false, defaultValue = "") String type) {
        return ResponseEntity.ok().body(notificationService.pagingNotification(type, pageable));
    }

    @GetMapping("/markAllRead")
    @Operation(summary = "Mark All Notification Read By User", tags = { "Notification Management" })
    public ResponseEntity<?> markAllRead() {
        return ResponseEntity.ok().body(notificationService.markAllRead());
    }

    @GetMapping("/markAllChecked")
    @Operation(summary = "Mark All Notification Checked By User", tags = { "Notification Management" })
    public void markAllCheck() {
        notificationService.markAllCheck();
    }

    @GetMapping("/markAsRead")
    @Operation(summary = "Mark As Read Notification By User And Id", tags = { "Notification Management" })
    public ResponseEntity<?> markAsRead(@RequestParam Long id) {
        return ResponseEntity.ok().body(notificationService.markAsRead(id));
    }

    @GetMapping("/countAllUnread")
    @Operation(summary = "Count All UnRead Notification By User", tags = { "Notification Management" })
    public ResponseEntity<?> countAllUnread() {
        return ResponseEntity.ok().body(notificationService.countAllUnRead());
    }

    @GetMapping("/save/{connectomeId}")
    @Operation(summary = "Count All UnRead Notification By User", tags = { "Notification Management" })
    public void saveNotification(@PathVariable String connectomeId) throws InterruptedException {
        String thatConnectomeId = StringUtil.isNullOrEmpty(connectomeId) ? "CID-4507f712-66e1-43da-91c2-08e33821ae78" : connectomeId;
        List<String> receiverIds = Collections.singletonList(thatConnectomeId);
        notificationService.saveNotificationByAdminWithInterpolationType(
            receiverIds,
            Constants.NotificationName.LEARNING_COMPLETE,
            null,
            "22"
        );
    }

    @GetMapping("/getAllType")
    @Operation(summary = "Get All Notification Type", tags = { "Notification Management" })
    public ResponseEntity<?> getAllNotificationType() {
        return ResponseEntity.ok().body(notificationService.getAllNotificationType());
    }

    @DeleteMapping("/deleteReadNotification")
    @Operation(summary = "Delete Read Notification", tags = { "Notification Management" })
    public void deleteReadNotification() {
        notificationService.deleteReadNotification();
    }

    @DeleteMapping("/deleteAll")
    @Operation(summary = "Delete All Notification", tags = { "Notification Management" })
    public void deleteAllNotification() {
        notificationService.deleteAllNotification();
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Notification By Id", tags = { "Notification Management" })
    public void deleteAllNotification(@PathVariable Long id) {
        notificationService.deleteById(id);
    }
}
