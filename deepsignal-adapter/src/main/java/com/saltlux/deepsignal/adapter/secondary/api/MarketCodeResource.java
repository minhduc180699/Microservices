package com.saltlux.deepsignal.adapter.secondary.api;

import com.saltlux.deepsignal.adapter.secondary.repository.MarketCodeMasterRepository;
import com.saltlux.deepsignal.adapter.secondary.service.MarketCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/marketCode")
@Tag(name = "Market Code Master Management", description = "Market Code Master API")
public class MarketCodeResource {

    private final MarketCodeMasterRepository marketCodeRepository;
    private final MarketCodeService marketCodeService;

    public MarketCodeResource(MarketCodeMasterRepository marketCodeRepository, MarketCodeService marketCodeService) {
        this.marketCodeRepository = marketCodeRepository;
        this.marketCodeService = marketCodeService;
    }

    @GetMapping("/search")
    @Operation(summary = "Search MarketCode by symbolCode, SymbolNameEn, SymbolNameKr", tags = { "Market Code Master Management" })
    public ResponseEntity<?> searchMarketCode(@RequestParam("search") String search) {
        return ResponseEntity.ok().body(marketCodeService.SearchMarketCode(search));
    }
}
