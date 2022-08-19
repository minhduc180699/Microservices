package com.saltlux.deepsignal.web.api.adapter;

import com.saltlux.deepsignal.web.aop.userActivities.UserActivity;
import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.service.dto.ApiResponse;
import com.saltlux.deepsignal.web.service.dto.ConnectomeImagePeopleDTO;
import com.saltlux.deepsignal.web.service.dto.PeopleAndCompanyDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/people")
@Tag(name = "People Management", description = "The People management API")
public class PeopleResource {

    @Autowired
    private RestTemplate restTemplate;

    private final String API;

    private final String APIIConnectome;

    private final ApplicationProperties applicationProperties;

    public PeopleResource(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        this.API = this.applicationProperties.getExternalApi().getDeepsignalAdapter();
        this.APIIConnectome = this.applicationProperties.getExternalApi().getDeepsignalConnectome();
    }

    @GetMapping("/getNetworkChart")
    @Operation(summary = "Get Network Chart for JSON Data", tags = { "People management" })
    public ResponseEntity<?> getNetworkChart(
        @RequestParam(value = "title") String title,
        @RequestParam(value = "language") String language
    ) {
        StringBuilder url = new StringBuilder();
        url.append(applicationProperties.getExternalApi().getDeepsignalEntityNetwork() + "/entitynetwork");
        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(url.toString())
            .queryParam("entityTitle", title)
            .queryParam("lang", language.toLowerCase())
            .build()
            .toUriString();
        Object result = restTemplate.getForObject(urlTemplate, Object.class);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/company")
    @Operation(summary = "Get People And Company Information for JSON Data", tags = { "People management" })
    public ResponseEntity<?> getContentsAndImages(@RequestBody PeopleAndCompanyDTO peopleAndCompanyDTO) {
        HttpEntity<PeopleAndCompanyDTO> request = new HttpEntity<>(peopleAndCompanyDTO);
        peopleAndCompanyDTO.setSourceLang(peopleAndCompanyDTO.getSourceLang().toLowerCase());
        String result = restTemplate.postForObject(
            applicationProperties.getExternalApi().getDeepsignalEntityNetwork() + "/entity/multipleGet",
            request,
            String.class
        );

        return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/market/name")
    @Operation(summary = "Get Market Information for JSON Data", tags = { "People management" })
    public ResponseEntity<?> getMarketName(@RequestParam(value = "code") String code) {
        String country = "US";
        if (code.contains("KS") || code.contains("KQ")) {
            country = "KR";
        }

        String url = applicationProperties.getExternalApi().getDeepsignalStock() + "/stock/readMaster/stock/" + country + "/" + code;
        String result = restTemplate.getForObject(url, String.class);

        return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{connectomeId}")
    @Operation(summary = "Get People by connectomeId", tags = { "People management" })
    public ResponseEntity<?> findByConnectomeId(
        Pageable pageable,
        @PathVariable("connectomeId") String connectomeId,
        @RequestParam("isGetStock") boolean isGetStock,
        @RequestParam("lang") String lang
    ) {
        if (StringUtils.isEmpty(connectomeId)) {
            return new ResponseEntity(new ApiResponse(false, "ConnectomeId is null"), HttpStatus.BAD_REQUEST);
        }
        StringBuilder url = new StringBuilder();
        url.append(API).append(Constants.FIND_PEOPLE).append("/").append(connectomeId);
        String sort = "";
        for (Sort.Order order : pageable.getSort()) {
            sort = order.getProperty() + "," + order.getDirection();
        }
        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(url.toString())
            .queryParam("page", pageable.getPageNumber())
            .queryParam("size", pageable.getPageSize())
            .queryParam("sort", StringUtils.isNotBlank(sort) ? sort : "id,DESC")
            .queryParam("isGetStock", isGetStock)
            .queryParam("lang", lang)
            .build()
            .toUriString();
        try {
            HttpEntity<JSONObject> response = restTemplate.getForEntity(urlTemplate, JSONObject.class);
            return ResponseEntity.ok().body(response.getBody());
        } catch (Exception e) {
            return new ResponseEntity(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/getImages")
    @Operation(summary = "Get all images for everyone", tags = { "People management" })
    public ResponseEntity<?> getImagePeople(@RequestBody List<String> titleList) {
        if (Objects.isNull(titleList)) {
            return new ResponseEntity(new ApiResponse(false, "Title is null"), HttpStatus.BAD_REQUEST);
        }
        ConnectomeImagePeopleDTO connectome = new ConnectomeImagePeopleDTO("", titleList);
        StringBuilder url = new StringBuilder();
        url.append(APIIConnectome).append(Constants.GET_IMAGES);
        HttpEntity<ConnectomeImagePeopleDTO> entity = new HttpEntity<>(connectome);
        try {
            ResponseEntity<Object[]> response = restTemplate.postForEntity(url.toString(), entity, Object[].class);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return new ResponseEntity(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/getImage")
    @Operation(summary = "Get image for each people", tags = { "People management" })
    public ResponseEntity<?> getImagePeople(@RequestParam("title") String title) {
        if (StringUtils.isEmpty(title)) {
            return new ResponseEntity(new ApiResponse(false, "Title is null"), HttpStatus.BAD_REQUEST);
        }
        ConnectomeImagePeopleDTO connectome = new ConnectomeImagePeopleDTO(title, null);
        StringBuilder url = new StringBuilder();
        url.append(APIIConnectome).append(Constants.GET_IMAGE);
        HttpEntity<ConnectomeImagePeopleDTO> entity = new HttpEntity<>(connectome);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url.toString(), entity, String.class);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return new ResponseEntity(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/entity")
    @Operation(summary = "get the WIKI information", tags = { "People Management" })
    @UserActivity(activityName = Constants.UserActivities.VISIT_PEOPLE)
    public ResponseEntity<?> getEntityByTitle(
        @RequestParam("title") String title,
        @RequestParam(value = "language", defaultValue = "EN") String language,
        @RequestParam(value = "connectomeId", required = false) String connectomeId
    ) {
        try {
            JSONObject param = new JSONObject();
            JSONArray titles = new JSONArray();
            titles.add(title);
            param.put("titles", titles);
            param.put("sourceLang", language.toLowerCase());
            param.put("getOnlyImages", false);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setContentLength(param.toString().length());

            HttpEntity<String> request = new HttpEntity<String>(param.toString(), headers);

            Object result = restTemplate.postForObject(
                applicationProperties.getExternalApi().getDeepsignalEntityNetwork() + "/entity/multipleGet",
                request,
                Object.class
            );

            return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/hidden/{connectomeId}")
    @Operation(summary = "Hidden People By Id", tags = { "People Management" })
    public void hiddenPeopleById(
        @PathVariable String connectomeId,
        @RequestParam String title,
        @RequestParam String type,
        @RequestParam String lang,
        @RequestParam(name = "deleted", defaultValue = "true", required = false) boolean deleted
    ) {
        String url = API + "/peoples/hidden/" + connectomeId;
        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(url)
            .queryParam("title", title)
            .queryParam("type", type)
            .queryParam("lang", lang.toLowerCase())
            .queryParam("deleted", deleted)
            .build()
            .toUriString();
        restTemplate.getForEntity(urlTemplate, JSONObject.class);
    }
}
