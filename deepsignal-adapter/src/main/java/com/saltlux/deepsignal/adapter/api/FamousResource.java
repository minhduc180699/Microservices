package com.saltlux.deepsignal.adapter.api;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.saltlux.deepsignal.adapter.domain.FamousCompany;
import com.saltlux.deepsignal.adapter.domain.FamousPeople;
import com.saltlux.deepsignal.adapter.service.IFamousCompanyService;
import com.saltlux.deepsignal.adapter.service.IFamousPeopleService;

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
@RequestMapping("/api/famous")
@Tag(name = "Famous Details Management", description = "The Famous Details Management API")
public class FamousResource {

    @Autowired
    private IFamousCompanyService iFamousCompanyService;

    @Autowired
    private IFamousPeopleService iFamousPeopleService;

    @GetMapping("/get")
    @Operation(summary = "Get the famous infos by Title", tags = { "Famous Details Management" })
    public ResponseEntity<?> getFamousCompanyByTitle(
            @RequestParam(value = "title", defaultValue = "") String title) {
        if (Objects.isNull(title)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            String decodedTitle=URLDecoder.decode( title, "UTF-8" );
            decodedTitle = decodedTitle.replace('_',' ');
            Map<String, Object> response = new HashMap<>();
            response.put("title", decodedTitle);
            FamousCompany famousCompanyResult =!iFamousCompanyService.findByTitle(decodedTitle).isPresent() ? null : iFamousCompanyService.findByTitle(decodedTitle).get();
            response.put("companyCandidate", famousCompanyResult);
            FamousPeople famousPeopleResult =!iFamousPeopleService.findByTitle(decodedTitle).isPresent() ? null : iFamousPeopleService.findByTitle(decodedTitle).get();
            response.put("peopleCandidate", famousPeopleResult);
            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
