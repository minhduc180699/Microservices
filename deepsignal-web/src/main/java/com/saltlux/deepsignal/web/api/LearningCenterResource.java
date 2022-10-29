package com.saltlux.deepsignal.web.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saltlux.deepsignal.web.aop.userActivities.UserActivity;
import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.service.IFileStorageService;
import com.saltlux.deepsignal.web.service.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/learning")
@Tag(name = "Learning Center Management", description = "The Learning Center management API")
public class LearningCenterResource {

    private final String API;

    private final RestTemplate restTemplate;

    private final String APIMetaSearch;

    private final String metasearchDatapterAPI;

    private static final int DURATION = 300000;

    private long activatedAt = Long.MAX_VALUE;

    private boolean isRealtimeSearch = false;

    private final IFileStorageService storageService;

    private static final Logger log = LoggerFactory.getLogger(LearningCenterResource.class);

    public LearningCenterResource(RestTemplate restTemplate, ApplicationProperties properties, IFileStorageService fileStorageService) {
        this.restTemplate = restTemplate;
        this.API = properties.getExternalApi().getDeepsignalAdapter();
        this.APIMetaSearch = properties.getExternalApi().getDeepsignalMetasearch();
        this.metasearchDatapterAPI = properties.getExternalApi().getDeepsignalAdapter();
        this.storageService = fileStorageService;
        activatedAt = System.currentTimeMillis();
    }

    @GetMapping("/search/{connectomeId}/{language}/{page}/{keyword}")
    @Operation(summary = "Return the search result from BigO system", tags = { "Learning Center Management" })
    @UserActivity(activityName = Constants.UserActivities.SEARCH_LEARNING)
    public ResponseEntity<?> getSearchResults(
        @PathVariable("connectomeId") String connectomeId,
        @PathVariable("language") String language,
        @PathVariable("page") int page,
        @PathVariable("keyword") String keyword,
        @RequestParam(name = "searchType", defaultValue = "") String searchType,
        @RequestParam(name = "channel", defaultValue = "google") String channel
    ) {
        //        dataTransfer.setLanguage(language);
        //        SearchResponse searchResponse = new SearchResponse();
        //        StringBuilder url = new StringBuilder();
        //        url.append(API).append(Constants.BIGO_SEARCH_URI).append("?query=").append(query);
        //        url.append(API).append(Constants.BIGO_SEARCH_URI);
        //        HttpEntity<DataBigOTransferDTO> request = new HttpEntity<DataBigOTransferDTO>(dataTransfer);
        //        try {
        //            ResponseEntity<JSONObject> response = restTemplate.postForEntity(url.toString(), request, JSONObject.class);
        //            searchResponse.setInternalSearch(response);
        //        } catch (Exception e) {
        //          e.printStackTrace();
        //        }
        //        try {
        //            if (StringUtils.isEmpty(dataTransfer.getScrollId())) {
        //                MetaSearchDTO metaSearchDTO = new MetaSearchDTO(
        //                    dataTransfer.getQuery(),
        //                    connectomeId,
        //                    language,
        //                    Constants.MetaSearchType.news.toString(),
        //                    page
        //                );
        //
        //                ResponseEntity<JSONObject> response1 = this.getResponse(metaSearchDTO, true);
        //                searchResponse.setMetaSearch(response1);
        //            }
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        SearchResponse searchResponse = new SearchResponse();
        if (StringUtils.isEmpty(keyword) || StringUtils.isEmpty(connectomeId) || StringUtils.isEmpty(language)) {
            return ResponseEntity.badRequest().body("At least one field is null");
        }
        MetaSearchDTO metaSearchDTO = new MetaSearchDTO();
        metaSearchDTO.setPage(page);
        metaSearchDTO.setLang(language);
        metaSearchDTO.setConnectomeId(connectomeId);
        metaSearchDTO.setKeyword(keyword);
        metaSearchDTO.setSearchType(searchType);
        metaSearchDTO.setChannel(channel);
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            log.warn(dtf.format(now) + ": --------- Starting calling api Metasearch with keyword = " + keyword + " -------");
            ResponseEntity<JSONObject> response1 = getResponse(metaSearchDTO, true);
            searchResponse.setMetaSearch(response1);
            log.warn(dtf.format(now) + ":---------- End calling api --------- ");
            return ResponseEntity.ok().body(searchResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/search-cache/{connectomeId}/{language}/{page}")
    @Operation(summary = "Return the search result from BigO system", tags = { "Learning Center Management" })
    public SearchResponse getMetasearchCacheResults(
        @PathVariable("connectomeId") String connectomeId,
        @PathVariable("language") String language,
        @PathVariable("page") int page,
        @RequestBody DataBigOTransferDTO dataTransfer
    ) {
        dataTransfer.setLanguage(language);
        SearchResponse searchResponse = new SearchResponse();
        try {
            if (StringUtils.isEmpty(dataTransfer.getScrollId())) {
                ResponseEntity<JSONObject> response;
                MetaSearchDTO metaSearchDTO = new MetaSearchDTO(
                    dataTransfer.getQuery(),
                    connectomeId,
                    language,
                    Constants.MetaSearchType.news.toString(),
                    page,
                    null
                );

                if (!isRealtimeSearch) {
                    response = this.getResponse(metaSearchDTO, false);
                    ArrayList<Object> arrayList = (ArrayList<Object>) response.getBody().get("data");

                    // In case the cache data hasn't data -> call google metasearch API to get data
                    if (arrayList.size() == 0 && page < 3) {
                        StringBuilder urlMetasearch = new StringBuilder();
                        urlMetasearch.append(APIMetaSearch).append(Constants.META_SEARCH);
                        metaSearchDTO.setPage(page + 1);
                        response = this.getResponse(metaSearchDTO, true);
                        this.isRealtimeSearch = true;
                    }
                } else {
                    response = this.getResponse(metaSearchDTO, true);
                }

                // Check after 5 minutes -> reset isRealtimeSearch avoid always call google API
                if (isActive()) {
                    this.isRealtimeSearch = false;
                    this.activate();
                }

                searchResponse.setMetaSearch(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchResponse;
    }

    private ResponseEntity<JSONObject> getResponse(MetaSearchDTO metaSearchDTO, boolean isGoogleRealtime) {
        StringBuilder urlSearch = new StringBuilder();

        if (isGoogleRealtime) {
            //            urlSearch.append(APIMetaSearch).append(Constants.META_SEARCH);
            urlSearch
                .append(APIMetaSearch)
                .append("/deepsignal/realtime/metasearch/getMetaSearch")
                .append("?keyword=" + metaSearchDTO.getKeyword())
                .append("&searchType=" + metaSearchDTO.getSearchType())
                .append("&lang=" + metaSearchDTO.getLang())
                .append("&session=[ { 'session_page':" + metaSearchDTO.getPage() + "}]")
                .append("&connectomeId=" + metaSearchDTO.getConnectomeId())
                .append("&channel=" + metaSearchDTO.getChannel());
            log.warn("Calling api /getMetaSearch...");
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlSearch.toString());
            return restTemplate.getForEntity(builder.toUriString(), JSONObject.class);
        } else {
            urlSearch.append(metasearchDatapterAPI).append(Constants.META_SEARCH_CACHE);
            HttpEntity<MetaSearchDTO> request = new HttpEntity<>(metaSearchDTO);
            log.warn("Calling api /metasearch-cache...");
            return restTemplate.postForEntity(urlSearch.toString(), request, JSONObject.class);
        }
    }

    /**
     * {@code POST  /learning-center/{userId}/{connectomeId}} : upload document, search, web
     *
     * @param userId user_id of current user
     * @param connectomeId connectome_id of current connectome
     * @param docs docId
     * @param language language of website
     * @param uploadUrl list of url
     * @param localFiles file local uploaded
     * @param driveFiles file drive uploaded
     * @return
     */
    @PostMapping("/learning-center/{userId}/{connectomeId}")
    @Operation(summary = "Upload for SEARCH, DOC or WEB", tags = { "URL Storage Management" })
    @UserActivity(activityName = Constants.UserActivities.TRAINING)
    public ResponseEntity<?> uploadAll(
        @PathVariable("userId") String userId,
        @PathVariable("connectomeId") String connectomeId,
        @RequestParam(value = "docs", required = false) String docs,
        @RequestParam(value = "lang", required = false) String language,
        @RequestPart(value = "urls", required = false) String uploadUrl,
        @RequestPart(value = "localFiles", required = false) MultipartFile[] localFiles,
        @RequestPart(value = "driveFiles", required = false) MultipartFile[] driveFiles
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        UploadWrapper uploadWrapper = objectMapper.readValue(uploadUrl, UploadWrapper.class);
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(connectomeId)) {
            return new ResponseEntity(new ApiResponse(false, "UserId or ConnectomeId is null"), HttpStatus.BAD_REQUEST);
        }
        try {
            List<LearningDocument> learningDocuments = new ArrayList<>();
            if (Objects.nonNull(localFiles) && localFiles.length > 0) {
                learningDocuments.add(new LearningDocument("local", localFiles));
            }
            if (Objects.nonNull(driveFiles) && driveFiles.length > 0) {
                learningDocuments.add(new LearningDocument("drive", driveFiles));
            }
            return ResponseEntity
                .ok()
                .body(
                    storageService.saveAllFileInfos(
                        learningDocuments,
                        uploadWrapper.getUrlUploadDTOS(),
                        docs,
                        userId,
                        connectomeId,
                        language
                    )
                );
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(new ApiResponse(false, "Save fail!"), HttpStatus.BAD_REQUEST);
        }
    }

    @Data
    public class SearchResponse {

        private ResponseEntity<JSONObject> internalSearch;
        private ResponseEntity<JSONObject> metaSearch;
    }

    public void activate() {
        activatedAt = System.currentTimeMillis();
    }

    public boolean isActive() {
        long activeFor = System.currentTimeMillis() - activatedAt;
        boolean check = activeFor >= 0 && activeFor > DURATION;
        return check;
    }
}
