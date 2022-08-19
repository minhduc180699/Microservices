package com.saltlux.deepsignal.adapter.api;

import com.saltlux.deepsignal.adapter.service.ISignalTodayIssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signal")
@Tag(name = "Signal Today Issue Management", description = "The signal today issue management API")
public class SignalTodayIssueResource {

    private final ISignalTodayIssueService signalTodayIssueService;

    public SignalTodayIssueResource(ISignalTodayIssueService signalTodayIssueService) {
        this.signalTodayIssueService = signalTodayIssueService;
    }

    @GetMapping("/paging/{connectomeId}")
    @Operation(summary = "Get Signal Today Issue By Paging", tags = { "Signal Today Issue Management" })
    public ResponseEntity<?> paging(
        Pageable pageable,
        @PathVariable String connectomeId,
        @RequestParam("work_day") String workDay,
        @RequestParam("signalType") String signalType
    ) {
        try {
            return ResponseEntity.ok().body(signalTodayIssueService.paging(pageable, connectomeId, workDay, signalType));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete Signal Today Issue By id", tags = { "Signal Today Issue Management" })
    public ResponseEntity<?> delete(
        @RequestParam("id") String id
    ) {
        try {
            signalTodayIssueService.deleteById(id);
            return ResponseEntity.ok().body(true);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
