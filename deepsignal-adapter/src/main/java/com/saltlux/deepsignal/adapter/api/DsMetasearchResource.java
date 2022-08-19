package com.saltlux.deepsignal.adapter.api;

import com.saltlux.bigo.types.Language;
import com.saltlux.deepsignal.adapter.domain.dstornado.DsRealtimeMetaSearch;
import com.saltlux.deepsignal.adapter.service.IDsRealtimeMetasearchService;
import com.saltlux.deepsignal.adapter.service.dto.MetaSearchDTO;
import com.saltlux.deepsignal.adapter.service.impl.DsRealtimeMetasearchServiceImpl;
import com.saltlux.deepsignal.adapter.util.Utility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metasearch-cache/")
@Tag(name = "Metasearch Cache Management", description = "The metasearch cache management API")
public class DsMetasearchResource {

    @Autowired
    private IDsRealtimeMetasearchService dsRealtimeMetasearchService;

    @GetMapping("/{lang}")
    @Operation(summary = "Get metasearch data by keyword", tags = { "Metasearch Cache Management" })
    public ResponseEntity<Map<String, Object>> getMetasearchData(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "_id") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
        @PathVariable("lang") String lang,
        @RequestParam("keyword") String keyword
    ) {
        if (Objects.isNull(lang) || Objects.isNull(keyword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Utility utility = new Utility(lang);
        try {
            Map<String, Object> response = new HashMap<>();
            Page<DsRealtimeMetaSearch> metaSearchEns = dsRealtimeMetasearchService.findByKeyword(
                page,
                size,
                orderBy,
                sortDirection,
                keyword
            );

            response.put("results", metaSearchEns.getContent());
            response.put("currentPage", metaSearchEns.getNumber());
            response.put("totalItems", metaSearchEns.getTotalElements());
            response.put("totalPages", metaSearchEns.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @Operation(summary = "Get metasearch data by keyword", tags = { "Metasearch Cache Management" })
    public ResponseEntity<Map<String, Object>> postMetasearchData(@RequestBody MetaSearchDTO metaSearchDTO) {
        if (Objects.isNull(metaSearchDTO) || Objects.isNull(metaSearchDTO.getKeyword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Utility utility = new Utility(metaSearchDTO.getLang());
        try {
            Map<String, Object> response = new HashMap<>();
            Page<DsRealtimeMetaSearch> metaSearchEns = dsRealtimeMetasearchService.findByKeyword(
                metaSearchDTO.getPage(),
                20,
                "_id",
                "desc",
                metaSearchDTO.getKeyword()
            );

            response.put("data", metaSearchEns.getContent());
            response.put("currentPage", metaSearchEns.getNumber());
            response.put("totalItems", metaSearchEns.getTotalElements());
            response.put("totalPages", metaSearchEns.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
