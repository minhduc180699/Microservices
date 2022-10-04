package com.saltlux.deepsignal.web.api.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saltlux.deepsignal.web.aop.userActivities.UserActivity;
import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.service.dto.FilterFeedDTO;
import com.saltlux.deepsignal.web.service.dto.PersonalDocumentRes;
import com.saltlux.deepsignal.web.util.ConnectAdapterApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/personal-documents")
@Tag(name = "Personal Documents Management", description = "The personal document management API")
public class PersonalDocumentResource {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ConnectAdapterApi connectAdapterApi;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{connectomeId}")
    public ResponseEntity<?> getPersonalDocumentById(
        @RequestParam("id") String personalDocumentId,
        @PathVariable("connectomeId") String connectomeId
    ) {
        String uri = connectAdapterApi.getExternalApi() + "/personal-documents/" + connectomeId;
        Map<String, Object> params = new HashMap<>();
        params.put("id", personalDocumentId);
        return new ResponseEntity<>(connectAdapterApi.getDataFromAdapterApi(uri, params, HttpMethod.GET), HttpStatus.OK);
    }

    @GetMapping("/getListDocuments/{connectomeId}")
    public ResponseEntity<?> getListPersonalDocuments(
        @PathVariable(value = "connectomeId") String connectomeId,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "20") int size,
        @RequestParam(value = "orderBy", defaultValue = "createdDate") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
        @RequestParam(value = "uploadType", defaultValue = "") String uploadType,
        @RequestParam(value = "isDelete", defaultValue = "0") int isDelete
    ) {
        try {
            String url =
                applicationProperties.getExternalApi().getDeepsignalAdapter() + "/personal-documents/getListDocuments/" + connectomeId;
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
            uriComponentsBuilder.queryParam("page", page);
            uriComponentsBuilder.queryParam("size", size);
            uriComponentsBuilder.queryParam("orderBy", orderBy);
            uriComponentsBuilder.queryParam("sortDirection", sortDirection);
            uriComponentsBuilder.queryParam("uploadType", uploadType);
            uriComponentsBuilder.queryParam("isDelete", isDelete);
            ResponseEntity<?> responseEntity = restTemplate.getForEntity(
                uriComponentsBuilder.toUriString(),
                Object.class,
                uriComponentsBuilder
            );
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.OK).body(responseEntity.getBody());
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/getListDocuments/{connectomeId}")
    @Operation(summary = "Get all feed information from deepsignal adapter by ConnectomeId", tags = { "Connectome Feed Management" })
    @UserActivity(activityName = Constants.UserActivities.UNKNOWN_SEARCH_FILTER_SORT)
    public ResponseEntity<?> getAllPersonalDocuments(
        @PathVariable(value = "connectomeId") String connectomeId,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "recommendDate") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
        @RequestParam(value = "uploadType", defaultValue = "") String uploadType,
        @RequestParam(value = "isDelete", defaultValue = "0") int isDelete,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "entityLabel", required = false) String entityLabel,
        @RequestBody List<FilterFeedDTO> filterFeedDTOS
    ) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("page", page);
            params.put("size", size);
            params.put("orderBy", orderBy);
            params.put("sortDirection", sortDirection);
            if (!"".equals(uploadType) && uploadType != null) {
                params.put("uploadType", uploadType);
            }
            if (!"".equals(keyword) && keyword != null) {
                params.put("keyword", keyword);
            }
            if (!"".equals(entityLabel) && entityLabel != null) {
                params.put("entityLabel", entityLabel);
            }
            Map<String, Object> uriParams = new HashMap<>();
            uriParams.put("connectomeId", connectomeId);
            String strJson;
            String uri = connectAdapterApi.getExternalApi() + "/personal-documents/getListDocuments/{connectomeId}";
            strJson = connectAdapterApi.getDataFromAdapterApi(uri, params, HttpMethod.POST, uriParams, filterFeedDTOS);
            return ResponseEntity.status(HttpStatus.OK).body(strJson);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getListDeletedDocuments/{connectomeId}")
    public ResponseEntity<?> getListDeletedDocuments(
        @PathVariable(value = "connectomeId") String connectomeId,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "20") int size,
        @RequestParam(value = "orderBy", defaultValue = "published_at") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection
    ) {
        try {
            String url =
                applicationProperties.getExternalApi().getDeepsignalAdapter() +
                "/personal-documents/getListDeletedDocuments/" +
                connectomeId;
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
            uriComponentsBuilder.queryParam("page", page);
            uriComponentsBuilder.queryParam("size", size);
            uriComponentsBuilder.queryParam("orderBy", orderBy);
            uriComponentsBuilder.queryParam("sortDirection", sortDirection);
            ResponseEntity<?> responseEntity = restTemplate.getForEntity(
                uriComponentsBuilder.toUriString(),
                Object.class,
                uriComponentsBuilder
            );
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.OK).body(responseEntity.getBody());
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        return ResponseEntity.ok().body(null);
    }

    @UserActivity(activityName = Constants.UserActivities.DELETE)
    @PostMapping("/deleteDocuments/{connectomeId}")
    public ResponseEntity<?> deleteDocuments(@RequestBody List<String> docIds, @PathVariable("connectomeId") String connectomeId) {
        try {
            String uri =
                applicationProperties.getExternalApi().getDeepsignalAdapter() + "/personal-documents/deleteDocuments/" + connectomeId;
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(uri);
            HttpEntity<?> entity = new HttpEntity<>(docIds);
            Map<String, Object> uriParam = new HashMap<>();
            ResponseEntity<String> response = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.DELETE,
                entity,
                new ParameterizedTypeReference<String>() {},
                uriParam
            );
            if (response.getStatusCode().is2xxSuccessful()) return ResponseEntity
                .status(HttpStatus.OK)
                .body(response.getBody()); else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getBody());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/getByIds")
    @Operation(summary = "Get all personal document by many id", tags = { "Personal Documents Management" })
    public ResponseEntity<?> getByIds(
        @RequestBody List<String> ids,
        @RequestParam(value = "isDeleted", required = false) Boolean isDeleted
    ) {
        try {
            StringBuilder uriPersonalDocument = new StringBuilder();
            uriPersonalDocument.append(connectAdapterApi.getExternalApi() + "/personal-documents/getByIds");
            if (isDeleted != null) uriPersonalDocument.append("?isDeleted=" + isDeleted);
            String response = connectAdapterApi.getDataFromAdapterApi(uriPersonalDocument.toString(), null, HttpMethod.POST, null, ids);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/getByDocIds")
    @Operation(summary = "Get all personal document by many id", tags = { "Personal Documents Management" })
    public ResponseEntity<?> getByDocIds(
        @RequestBody List<String> ids,
        @RequestParam(value = "isDeleted", required = false) Boolean isDeleted
    ) {
        try {
            StringBuilder uriPersonalDocument = new StringBuilder();
            uriPersonalDocument.append(connectAdapterApi.getExternalApi() + "/personal-documents/getByDocIds");
            if (isDeleted != null) uriPersonalDocument.append("?isDeleted=" + isDeleted);
            String response = connectAdapterApi.getDataFromAdapterApi(uriPersonalDocument.toString(), null, HttpMethod.POST, null, ids);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
