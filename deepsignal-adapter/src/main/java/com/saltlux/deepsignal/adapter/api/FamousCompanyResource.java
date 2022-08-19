package com.saltlux.deepsignal.adapter.api;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.saltlux.deepsignal.adapter.domain.FamousCompany;
import com.saltlux.deepsignal.adapter.service.IFamousCompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/famous-company")
@Tag(name = "Famous Company Details Management", description = "The Famous Company Details Management API")
public class FamousCompanyResource {

    @Autowired
    private IFamousCompanyService iFamousCompanyService;

    @GetMapping("/getAll")
    @Operation(summary = "Get all famous companies", tags = { "Famous Company Details Management" })
    public ResponseEntity<Map<String, Object>> getAllConnectomeFeed(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "orderBy", defaultValue = "score") String orderBy,
            @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection) {
        try {
            Page<FamousCompany> famousCompanyPage = iFamousCompanyService.findAll(page, size, orderBy, sortDirection);
            Map<String, Object> response = new HashMap<>();
            response.put("famousCompanies", famousCompanyPage.getContent());
            response.put("currentPage", famousCompanyPage.getNumber());
            response.put("totalItems", famousCompanyPage.getTotalElements());
            response.put("totalPages", famousCompanyPage.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get")
    @Operation(summary = "Get the famous company by Title", tags = { "Famous Company Details Management" })
    public ResponseEntity<FamousCompany> getFamousCompanyByTitle(
            @RequestParam(value = "title", defaultValue = "") String title) {
        if (Objects.isNull(title)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            String decodedTitle=URLDecoder.decode( title, "UTF-8" );
            decodedTitle = decodedTitle.replace('_',' ');
            return new ResponseEntity<>(
                    !iFamousCompanyService.findByTitle(decodedTitle).isPresent() ? null : iFamousCompanyService.findByTitle(decodedTitle).get(),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
