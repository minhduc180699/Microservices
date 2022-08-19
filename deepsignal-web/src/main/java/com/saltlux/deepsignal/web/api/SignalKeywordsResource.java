package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.domain.SignalKeywords;
import com.saltlux.deepsignal.web.service.ISignalKeywordsService;
import com.saltlux.deepsignal.web.service.dto.SignalKeywordsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signal-keywords/{connectomeId}")
@Tag(name = "Signal Keywords Management", description = "The Signal Keywords management API")
public class SignalKeywordsResource {

    private final Logger log = LoggerFactory.getLogger(SignalKeywordsResource.class);

    private ISignalKeywordsService signalKeywordsService;

    public SignalKeywordsResource(ISignalKeywordsService signalKeywordsService) {
        this.signalKeywordsService = signalKeywordsService;
    }

    @GetMapping("/getByConnectomeId")
    @Operation(summary = "Get signal keywords information by Connectome Id", tags = { "Signal Keywords Management" })
    public ResponseEntity<List<SignalKeywords>> getByConnectomeId(
        @PathVariable String connectomeId,
        @RequestParam(value = "status", defaultValue = "0") Integer status
    ) {
        log.debug("REST request to get all Signal keywords");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(signalKeywordsService.findSignalKeywordsByConnectomeIdAndStatus(connectomeId, status));
    }

    @GetMapping("/getById/{id}")
    @Operation(summary = "Get all signal keywords information", tags = { "Signal Keywords Management" })
    public ResponseEntity<Optional<SignalKeywords>> getById(@PathVariable String connectomeId, @PathVariable Long id) {
        log.debug("REST request to save Signal keywords : {} ", id);
        return ResponseEntity.status(HttpStatus.OK).body(signalKeywordsService.findById(id));
    }

    @PostMapping("/save")
    @Operation(summary = "Save signal keywords", tags = { "Signal Keywords Management" })
    public ResponseEntity<?> save(@RequestBody SignalKeywordsDTO signalKeywordsDTO, @PathVariable String connectomeId) throws Exception {
        log.debug("REST request to save Signal keywords : {} ", signalKeywordsDTO);
        return ResponseEntity.status(HttpStatus.OK).body(signalKeywordsService.save(signalKeywordsDTO, connectomeId));
    }

    @DeleteMapping("/{signalId}/{id}")
    @Operation(summary = "Delete signal keywords by id", tags = { "Signal Keywords Management" })
    public void save(@PathVariable String connectomeId, @PathVariable String id, @PathVariable(value = "signalId") Long signalId)
        throws Exception {
        log.debug("REST request to delete Signal keywords with id : {} ", signalId);
        signalKeywordsService.deleteByConnectomeIdAndId(id, signalId);
    }
}
