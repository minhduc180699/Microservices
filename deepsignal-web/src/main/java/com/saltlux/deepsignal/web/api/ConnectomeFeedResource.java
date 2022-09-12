package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.aop.userActivities.UserActivity;
import com.saltlux.deepsignal.web.api.client.DsAdapterClient;
import com.saltlux.deepsignal.web.api.vm.ConnectomeParams;
import com.saltlux.deepsignal.web.api.vm.StockPriceVM;
import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.domain.LinkShare;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.exception.AccountAlreadyNotExistException;
import com.saltlux.deepsignal.web.exception.AccountTemporaryException;
import com.saltlux.deepsignal.web.repository.ConnectomeRepository;
import com.saltlux.deepsignal.web.repository.LinkShareRepository;
import com.saltlux.deepsignal.web.service.IConnectomeFeedService;
import com.saltlux.deepsignal.web.service.MailService;
import com.saltlux.deepsignal.web.service.UserService;
import com.saltlux.deepsignal.web.service.dto.*;
import com.saltlux.deepsignal.web.service.impl.ConnectomeService;
import com.saltlux.deepsignal.web.util.ConnectAdapterApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/connectome-feed")
@Tag(name = "Connectome Feed Management", description = "The Connectome Feed management API")
public class ConnectomeFeedResource {

    @Autowired
    private ConnectAdapterApi connectAdapterApi;

    @Autowired
    private IConnectomeFeedService iConnectomeFeedService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private UserService userService;

    @Autowired
    private LinkShareRepository linkShareRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private ConnectomeRepository connectomeRepository;

    @Autowired
    private ConnectomeService connectomeService;

    private DsAdapterClient adapterClient;

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    public ConnectomeFeedResource(DsAdapterClient adapterClient) {
        this.adapterClient = adapterClient;
    }

    /**
     * {@code GET  /getAll} : get all connectome feed information from deepsignal adapter.
     *
     * @param page          current page
     * @param size          number object on a page
     * @param orderBy       field sorted
     * @param sortDirection sort ascending/descending
     * @return
     */
    @GetMapping("/getAll")
    @Operation(summary = "get all connectome feed information from deepsignal adapter", tags = { "Connectome Feed Management" },
        security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getAllConnectomeFeed(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "score") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection
    ) {
        try {
            return adapterClient.getAllConnectomeFeed(page, size, orderBy, sortDirection);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getByIds")
    @Operation(summary = "Get feed by id from deepsignal adapter", tags = { "Connectome Feed Management" }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getAllConnectomeFeedByConnectomeId(@RequestBody List<String> ids) {
        try {
//            String uri = connectAdapterApi.getExternalApi() + "/connectome-feed/getByIds";
//            String strJson = connectAdapterApi.getDataFromAdapterApi(uri, null, HttpMethod.POST, null, ids);
//            ObjectMapper objectMapper = new ObjectMapper();
//            FeedRes feedRes = objectMapper.readValue(strJson, FeedRes.class);
//            return new ResponseEntity<>(feedRes, HttpStatus.OK);
            return adapterClient.getByIds(ids);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getListFeeds/{connectomeId}")
    @Operation(summary = "Get all feed information from deepsignal adapter by ConnectomeId", tags = { "Connectome Feed Management" }, security = @SecurityRequirement(name = "bearerAuth"))
    @UserActivity(activityName = Constants.UserActivities.UNKNOWN_SEARCH_FILTER_SORT)
    public ResponseEntity<?> getAllFeedByConnectomeId(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "recommendDate") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
        @PathVariable(value = "connectomeId") String connectomeId,
        @RequestParam(value = "topic", defaultValue = "") String topic,
        @RequestParam(value = "excepted", defaultValue = "false") boolean excepted,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestBody List<FilterFeedDTO> filterFeedDTOS
    ) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("page", page);
            params.put("size", size);
            params.put("orderBy", orderBy);
            params.put("sortDirection", sortDirection);
            if (!"".equals(keyword) && keyword != null) {
                params.put("keyword", keyword);
            }
            Map<String, Object> uriParams = new HashMap<>();
            uriParams.put("connectomeId", connectomeId);
            String strJson;

            if ("".equals(topic) || topic == null) {
//                String uri = connectAdapterApi.getExternalApi() + "/connectome-feed/getListFeeds/{connectomeId}";
//                strJson = connectAdapterApi.getDataFromAdapterApi(uri, params, HttpMethod.POST, uriParams, filterFeedDTOS);
                return adapterClient.getFeedByConnectomeId(page, size, orderBy, sortDirection, keyword, connectomeId, filterFeedDTOS);
            } else {
//                String uri = connectAdapterApi.getExternalApi() + "/connectome-feed/getListFeeds/{connectomeId}/{topic}";
//                params.put("excepted", excepted);
//                uriParams.put("topic", topic);
//                strJson = connectAdapterApi.getDataFromAdapterApi(uri, params, HttpMethod.GET, uriParams);
                return adapterClient.getFeedByConnectomeIdAndTopic(page, size, orderBy, sortDirection, connectomeId, topic, excepted);
            }

//            ObjectMapper objectMapper = new ObjectMapper();
//            FeedRes feedRes = objectMapper.readValue(strJson, FeedRes.class);
//            return new ResponseEntity<>(feedRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("getFeed/{docId}")
    @Operation(summary = "get connectome feed information by docId", tags = { "Connectome Feed Management" }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getFeedByDocId(@PathVariable(value = "docId") String docId) {
        try {
            ConnectomeFeedDTO connectomeFeedDTO = iConnectomeFeedService.getFeedConnectomeByDocId(docId);
            return new ResponseEntity<>(connectomeFeedDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getStock")
    @Operation(summary = "Get stock price for Feed", tags = { "Connectome Feed Management" }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getStockPriceData(@Valid @RequestBody StockPriceVM stockPriceVM) {
        try {
            StringBuilder url = new StringBuilder(
                applicationProperties.getExternalApi().getDeepsignalStock() + "/stock/getStockPriceHistoryFromDB/"
            );
            url.append(stockPriceVM.getMarketName());
            url.append("/".concat(stockPriceVM.getSymbolCode()));
            url.append("/".concat(stockPriceVM.getInterval()));
            url.append("/".concat(stockPriceVM.getMaxRecord()));
            Object result = restTemplate.getForObject(url.toString(), Object.class);
            return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/training/{connectomeId}")
    @Operation(summary = "get all topic information", tags = { "Connectome Management" }, security = @SecurityRequirement(name = "bearerAuth"))
    @UserActivity(activityName = Constants.UserActivities.TRAINING)
    public ResponseEntity<?> trainingData(
        @PathVariable("connectomeId") String connectomeId,
        @RequestParam("sourceLang") String sourceLang
    ) {
        try {
            ConnectomeParams connectomeParams = new ConnectomeParams();
            connectomeParams.setConnectomeId(connectomeId);
            connectomeParams.setSourceLang(sourceLang.toLowerCase());
            //            connectomeParams.setTargetLangList(Collections.singletonList("KO"));
            Object result = restTemplate.postForObject(
                applicationProperties.getExternalApi().getDeepsignalConnectome() + Constants.CONNECTOME_TRAINING,
                connectomeParams,
                Object.class
            );
            return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/showByUrl/{connectomeId}/{feedId}")
    @Operation(summary = "Show content of website by url", tags = { "Connectome Feed Management" }, security = @SecurityRequirement(name = "bearerAuth"))
    @UserActivity(activityName = Constants.UserActivities.READ_ARTICLE)
    public ResponseEntity<?> previewByUrl(
        @RequestParam String url,
        @PathVariable("connectomeId") String connectomeId,
        @PathVariable("feedId") String feedId
    ) {
        try {
            String uri = this.applicationProperties.getExternalApi().getDeepsignalDocConverter() + Constants.URL_PREVIEW;
            Map<String, Object> params = new HashMap<>();
            params.put("url", url);
            String strJson = connectAdapterApi.getDataFromAdapterApi(uri, params, HttpMethod.GET);
            return ResponseEntity.ok().body(strJson);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/updateFeed/{connectomeId}")
    @Operation(
        summary = "API run in background, to send a request to run a job and update feed when done job",
        tags = { "Connectome Feed Management" },
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<?> updateFeed(@PathVariable("connectomeId") String connectomeId) {
        try {
            String uri = this.applicationProperties.getExternalApi().getDeepsignalDashboard() + Constants.RECOMMEND_JOB_FEED;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject(uri, new Wrapper.FeedWrapper(connectomeId), Wrapper.FeedWrapper.class);
            return ResponseEntity.ok().body(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/searchStock")
    @Operation(summary = "search stocks", tags = { "Connectome Feed Management" }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> searchStock(@RequestParam("search") String search) {
        StringBuilder url = new StringBuilder();
        url.append(applicationProperties.getExternalApi().getDeepsignalAdapter()).append("/marketCode/search");
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(url.toString()).queryParam("search", search).build().toUriString();
        try {
            HttpEntity<String> response = restTemplate.getForEntity(urlTemplate, String.class);
            return ResponseEntity.ok().body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/activity/{connectomeId}")
    @Operation(summary = "handle interaction", tags = { "Connectome Feed Management" }, security = @SecurityRequirement(name = "bearerAuth"))
    @UserActivity(activityName = Constants.UserActivities.UNKNOWN_ACTIVITY)
    public ResponseEntity<?> handleActivity(
        @RequestParam("docId") String docId,
        @RequestParam("state") boolean state,
        @RequestParam("activity") String activity,
        @PathVariable("connectomeId") String connectomeId,
        @RequestParam("page") String page,
        @RequestParam("likeState") int likeState,
        @RequestParam(value = "feedback", required = false, defaultValue = "") String feedback
    ) {
        if (StringUtils.isEmpty(docId) || StringUtils.isEmpty(activity)) {
            return ResponseEntity.badRequest().body("Id or activity is null");
        }
//        StringBuilder url = new StringBuilder();
//        url.append(applicationProperties.getExternalApi().getDeepsignalAdapter()).append("/connectome-feed/handleActivity");
//        String urlTemplate = UriComponentsBuilder
//            .fromHttpUrl(url.toString())
//            .queryParam("docId", docId)
//            .queryParam("connectomeId", connectomeId)
//            .queryParam("state", state)
//            .queryParam("activity", activity)
//            .queryParam("page", page)
//            .queryParam("likeState", likeState)
//            .build()
//            .toUriString();
        try {
//            HttpEntity<String> response = restTemplate.getForEntity(urlTemplate, String.class);
//            return ResponseEntity.ok().body(response.getBody());
            return adapterClient.handleActivity(docId, state, activity, connectomeId, page, likeState);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sharingCard")
    @Operation(summary = "handle share card", tags = { "Connectome Feed Management" }, security = @SecurityRequirement(name = "bearerAuth"))
    @UserActivity(activityName = Constants.UserActivities.SHARE)
    public ResponseEntity<?> handleSharingCard(
        @RequestParam("id") String id,
        @RequestParam("platform") String platform,
        @RequestParam(value = "connectomeId", required = false) String connectomeId
    ) {
        Boolean checkPlatform = false;
        for (Constants.PlatformSharing platformSharing : Constants.PlatformSharing.values()) {
            if (platform.equals(platformSharing.type)) {
                checkPlatform = true;
            }
        }
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(platform) || !checkPlatform) {
            return ResponseEntity.badRequest().body("");
        }
//        StringBuilder url = new StringBuilder();
//        url.append(applicationProperties.getExternalApi().getDeepsignalAdapter()).append("/connectome-feed/sharingCard");
//        String urlTemplate = UriComponentsBuilder
//            .fromHttpUrl(url.toString())
//            .queryParam("id", id)
//            .queryParam("platform", platform)
//            .build()
//            .toUriString();
        try {
//            HttpEntity<String> response = restTemplate.getForEntity(urlTemplate, String.class);
//            return ResponseEntity.ok().body(response.getBody());
            return adapterClient.handleSharing(id, platform);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getDetailCard/{connectomeId}")
    @Operation(summary = "Search card by id", tags = { "Connectome Feed Management" }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getDetailCard(@RequestParam("id") String id, @PathVariable("connectomeId") String connectomeId) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(connectomeId)) {
            return ResponseEntity.badRequest().body("ConnectomeId or id is null");
        }
//        StringBuilder url = new StringBuilder();
//        url.append(applicationProperties.getExternalApi().getDeepsignalAdapter()).append("/connectome-feed/getDetailCard");
//        String urlTemplate = UriComponentsBuilder.fromHttpUrl(url.toString()).queryParam("id", id).build().toUriString();
        try {
//            HttpEntity<String> response = restTemplate.getForEntity(urlTemplate, String.class);
//            return ResponseEntity.ok().body(response.getBody());
            return adapterClient.getDetailCard(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/saveAndSendEmail")
    @Operation(summary = "Save people and send email", tags = { "Connectome Feed Management" }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> saveAndSendEmail(
        @RequestParam("connectomeId") String connectomeId,
        @RequestParam("cardId") String cardId,
        @RequestBody MailParamsDTO mailParamsDTO
    ) {
        if (
            StringUtils.isEmpty(connectomeId) ||
            StringUtils.isEmpty(cardId) ||
            StringUtils.isEmpty(mailParamsDTO.getLinkShare()) ||
            ObjectUtils.isEmpty(mailParamsDTO.getEmails())
        ) {
            return ResponseEntity.badRequest().body("At least one field is null");
        }
        Connectome connectome = connectomeRepository.findConnectomeByConnectomeId(connectomeId).get();
        User user = connectome.getUser();
        try {
            mailService.sendEmailSharePost(user, mailParamsDTO);
            return ResponseEntity.ok().body("Send emails successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Send emails failed!");
        }
    }

    @GetMapping("/checkPermissionShare")
    @Operation(summary = "Check user's permission for link share by email", tags = { "Connectome Feed Management" }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> checkPermissionShare(
        @RequestParam("connectomeId") String connectomeId,
        @RequestParam("cardId") String cardId,
        @RequestParam("connectomeUserId") String connectomeUserId
    ) {
        if (StringUtils.isEmpty(connectomeId) || StringUtils.isEmpty(cardId)) {
            return ResponseEntity.badRequest().body("At least one field is null");
        }
        List<LinkShare> linkShares = new ArrayList<>();
        try {
            linkShares = linkShareRepository.findAllByConnectome_ConnectomeIdAndCardId(connectomeId, cardId);
            if (ObjectUtils.isEmpty(linkShares)) {
                return ResponseEntity.ok().body(true);
            }
            Connectome connectome = connectomeRepository.findConnectomeByConnectomeId(connectomeUserId).get();
            User user = connectome.getUser();
            Boolean check = false;
            for (LinkShare linkShare : linkShares) {
                if (linkShare.getEmail().equals(user.getEmail())) {
                    check = true;
                    break;
                }
            }
            if (check) {
                return ResponseEntity.ok().body(true);
            } else {
                return ResponseEntity.ok().body(false);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error is occurred. Please try again later!");
        }
    }

    @Operation(summary = "get the information connectome by current account ", tags = { "Connectome Management" }, security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/getByUserId")
    public ResponseEntity<?> getDataByLogin(@RequestParam("login") String login) {
        try {
            List<Connectome> connectomes = connectomeService.findByUserLogin(login);
            return new ResponseEntity<>(connectomes, new HttpHeaders(), HttpStatus.OK);
        } catch (AccountAlreadyNotExistException ae) {
            return new ResponseEntity(
                new ApiResponse(false, Constants.ErrorCode.DEEPSINAL_REGISTER_ACCOUNT_NOT_EXISTED.description),
                HttpStatus.NOT_FOUND
            );
        } catch (AccountTemporaryException at) {
            return new ResponseEntity(
                new ApiResponse(false, Constants.ErrorCode.DEEPSINAL_REGISTER_ACCOUNT_TEMPORARY.description),
                HttpStatus.FORBIDDEN
            );
        }
    }

    @Operation(summary = "Get Connectome Feed By Recommend Date", tags = { "Connectome Management" }, security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/getByRecommendDate/{connectomeId}")
    public ResponseEntity<?> getFeedByRecommendDate(
        @PathVariable String connectomeId,
        @RequestParam("lang") String lang,
        @RequestParam("recommend_date") String recommendDate
    ) throws ParseException {
//        String uri = connectAdapterApi.getExternalApi() + "/connectome-feed/getFeedByDate/{connectomeId}";
//        Map<String, Object> params = new HashMap<>();
//        params.put("lang", lang);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS'Z'");
        Date dateParse = formatter.parse(recommendDate);
        Instant dateConvert = dateParse.toInstant();
//        params.put("recommendDate", dateConvert);
//        Map<String, Object> uriParams = new HashMap<>();
//        uriParams.put("connectomeId", connectomeId);
//        String strJson = connectAdapterApi.getDataFromAdapterApi(uri, params, HttpMethod.GET, uriParams);
//        return ResponseEntity.ok().body(strJson);
        return adapterClient.getByConnectomeIdAndDate(connectomeId, lang, dateConvert);
    }

    public Long countNewFeedByDate(String connectomeId, Instant recommendDate) {
//        String uri = connectAdapterApi.getExternalApi() + "/connectome-feed/countFeedByDate/{connectomeId}";
//        Map<String, Object> params = new HashMap<>();
//        params.put("lang", lang);
//        params.put("recommendDate", recommendDate);
//        Map<String, Object> uriParams = new HashMap<>();
//        uriParams.put("connectomeId", connectomeId);
//        String strJson = connectAdapterApi.getDataFromAdapterApi(uri, params, HttpMethod.GET, uriParams);
        ResponseEntity<?> entity = adapterClient.countFeedByDate(connectomeId, recommendDate);
        return Long.parseLong(entity.getBody().toString());
    }

    @PostMapping("/getActivity/{connectomeId}")
    @Operation(summary = "get getActivity", tags = { "Connectome Management" }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getActivity(
        @PathVariable(value = "connectomeId") String connectomeId,
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam(value = "orderBy", defaultValue = "recommendDate") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
        @RequestBody List<FilterFeedDTO> filterFeedDTOS
    ) {
        if (filterFeedDTOS.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing filterFeedDTO");
        }
        Optional<User> user = userService.getUserWithAuthorities();
        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body("User not found");
        }

//        String uri = connectAdapterApi.getExternalApi() + "/connectome-feed/getActivity/{connectomeId}";
//        Map<String, Object> params = new HashMap<>();
//        params.put("page", page);
//        params.put("size", size);
//        params.put("orderBy", orderBy);
//        params.put("sortDirection", sortDirection);
//        params.put("lang", user.get().getLangKey());
//        Map<String, Object> uriParams = new HashMap<>();
//        uriParams.put("connectomeId", connectomeId);
//        String strJson = connectAdapterApi.getDataFromAdapterApi(uri, params, HttpMethod.POST, uriParams, filterFeedDTOS);
//        return ResponseEntity.ok().body(strJson);
        return adapterClient.getActivity(connectomeId, page, size, orderBy, sortDirection, user.get().getLangKey(), filterFeedDTOS);
    }
}
