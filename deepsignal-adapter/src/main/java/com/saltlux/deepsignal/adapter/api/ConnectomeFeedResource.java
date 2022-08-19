package com.saltlux.deepsignal.adapter.api;

import com.saltlux.deepsignal.adapter.config.Constants;
import com.saltlux.deepsignal.adapter.domain.ConnectomeFeed;
import com.saltlux.deepsignal.adapter.domain.Feed;
import com.saltlux.deepsignal.adapter.domain.PersonalDocument;
import com.saltlux.deepsignal.adapter.repository.dsservice.PersonalDocumentRepository;
import com.saltlux.deepsignal.adapter.service.IConnectomeFeedService;
import com.saltlux.deepsignal.adapter.service.IPersonalDocumentService;
import com.saltlux.deepsignal.adapter.service.dto.ActivityDTO;
import com.saltlux.deepsignal.adapter.service.dto.ActivityResponse;
import com.saltlux.deepsignal.adapter.service.dto.FilterFeedDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/connectome-feed")
@Tag(name = "Connectome Feed Management", description = "The connectome feed management API")
public class ConnectomeFeedResource {

    @Autowired
    private IConnectomeFeedService iConnectomeFeedService;

    @Autowired
    private IPersonalDocumentService ipersonalDocumentService;

    @Autowired
    private PersonalDocumentRepository personalDocumentRepository;

    @GetMapping("/getAll")
    @Operation(summary = "Get all connectome feed", tags = { "Connectome Feed Management" })
    public ResponseEntity<Map<String, Object>> getAllConnectomeFeed(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "score") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection
    ) {
        try {
            Page<ConnectomeFeed> connectomeFeedPage = iConnectomeFeedService.findAll(page, size, orderBy, sortDirection);
            Map<String, Object> response = new HashMap<>();
            response.put("connectomeFeeds", connectomeFeedPage.getContent());
            response.put("currentPage", connectomeFeedPage.getNumber());
            response.put("totalItems", connectomeFeedPage.getTotalElements());
            response.put("totalPages", connectomeFeedPage.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get connectome feed by Id", tags = { "Connectome Feed Management" })
    public ResponseEntity<ConnectomeFeed> getConnetomeFeedById(@PathVariable(value = "id") String id) {
        if (Objects.isNull(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            return new ResponseEntity<>(
                !iConnectomeFeedService.findById(id).isPresent() ? null : iConnectomeFeedService.findById(id).get(),
                HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getByIds")
    @Operation(summary = "Get connectome feed by feed ids", tags = { "Connectome Feed Management" })
    public ResponseEntity<?> getByIds(
        @RequestBody List<String> ids,
        @RequestParam(value = "isDeleted", required = false) Boolean isDeleted
    ) throws Exception {
        try {
            List<Feed> connectomeFeed = iConnectomeFeedService.findFeedByIds(ids, isDeleted);
            Map<String, Object> response = new HashMap<>();
            response.put("connectomeFeeds", connectomeFeed);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getListFeeds/{connectomeId}")
    @Operation(summary = "Get feed by connectome Id", tags = { "Connectome Feed Management" })
    public ResponseEntity<Map<String, Object>> getFeedByConnectomeId(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "recommendDate") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
        @RequestParam(value = "keyword", required = false) String keyword,
        @PathVariable("connectomeId") String connectomeId,
        @RequestBody List<FilterFeedDTO> filterFeedDTOS
    ) {
        if (Objects.isNull(connectomeId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            Page<Feed> connectomeFeedPage;
            String keywordDecoded = null;
            if (!"".equals(keyword) && keyword != null) {
                keywordDecoded = URLDecoder.decode(keyword, StandardCharsets.UTF_8.toString());
            }
            connectomeFeedPage =
                iConnectomeFeedService.findFeedByConnectomeIdKeywordAndFilter(
                    page,
                    size,
                    orderBy,
                    sortDirection,
                    connectomeId,
                    keywordDecoded,
                    filterFeedDTOS
                );
            Map<String, Object> response = new HashMap<>();
            response.put("connectomeFeeds", connectomeFeedPage.getContent());
            response.put("currentPage", connectomeFeedPage.getNumber());
            response.put("totalItems", connectomeFeedPage.getTotalElements());
            response.put("totalPages", connectomeFeedPage.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/countByConnectomeId/{connectomeId}")
    @Operation(summary = "Count all row feed by connectome Id", tags = { "Connectome Feed Management" })
    public ResponseEntity<Map<String, Object>> countFeedByConnectomeId(@PathVariable("connectomeId") String connectomeId) {
        if (Objects.isNull(connectomeId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            long totalItems = iConnectomeFeedService.countAllFeedByConnectomeId(connectomeId);
            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", totalItems);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getListFeeds/{connectomeId}/{topic}")
    @Operation(summary = "Get feed by connectome Id and topic", tags = { "Connectome Feed Management" })
    public ResponseEntity<Map<String, Object>> getFeedByConnectomeIdAndTopic(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "timestamp") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
        @PathVariable("connectomeId") String connectomeId,
        @PathVariable("topic") String topic,
        @RequestParam("excepted") boolean excepted
    ) {
        if (Objects.isNull(connectomeId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            Page<Feed> connectomeFeedPage = iConnectomeFeedService.findFeedByConnectomIdAndTopic(
                page,
                size,
                orderBy,
                sortDirection,
                connectomeId,
                topic,
                excepted
            );
            Map<String, Object> response = new HashMap<>();
            response.put("connectomeFeeds", connectomeFeedPage.getContent());
            response.put("currentPage", connectomeFeedPage.getNumber());
            response.put("totalItems", connectomeFeedPage.getTotalElements());
            response.put("totalPages", connectomeFeedPage.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/handleActivity")
    @Operation(summary = "handle event bookmark and delete", tags = { "Connectome Feed Management" })
    public ResponseEntity<?> handleBookmark(
        @RequestParam("docId") String docId,
        @RequestParam("state") boolean state,
        @RequestParam("activity") String activity,
        @RequestParam("connectomeId") String connectomeId,
        @RequestParam("page") String page,
        @RequestParam("likeState") int isLiked
    ) {
        if (StringUtils.isEmpty(docId) || StringUtils.isEmpty(activity) || StringUtils.isEmpty(connectomeId) || StringUtils.isEmpty(page)) {
            return ResponseEntity.badRequest().body("At lear a parameter is null");
        }
        if (iConnectomeFeedService.handleActivity(docId, state, activity, connectomeId, page, isLiked)) {
            return ResponseEntity.ok().body(null);
        } else {
            return ResponseEntity.badRequest().body("An error occurred. Please try again later!");
        }
    }

    @GetMapping("/sharingCard")
    @Operation(summary = "handle event share card", tags = { "Connectome Feed Management" })
    public ResponseEntity<?> handleSharing(@RequestParam("id") String id, @RequestParam("platform") String platform) {
        if (iConnectomeFeedService.handleShare(id, platform)) {
            return ResponseEntity.ok().body(null);
        } else {
            return ResponseEntity.badRequest().body("An error occurred. Please try again later!");
        }
    }

    @GetMapping("/getDetailCard")
    @Operation(summary = "Search card by id", tags = { "Connectome Feed Management" })
    public ResponseEntity<?> getDetailCard(@RequestParam("id") String id) {
        if (StringUtils.isEmpty(id)) {
            return ResponseEntity.badRequest().body("id is null");
        }
        Optional<Feed> feed = iConnectomeFeedService.findDetailCardById(id);
        if (feed != null) {
            return ResponseEntity.ok().body(feed.get());
        } else {
            Optional<PersonalDocument> personalDocument = personalDocumentRepository.findById(new ObjectId(id));
            if (personalDocument.isPresent()) {
                return ResponseEntity.ok().body(personalDocument.get());
            } else {
                return ResponseEntity.badRequest().body("document doesn't exist");
            }
        }
    }

    @PostMapping("/memo")
    @Operation(summary = "Handle feed memo", tags = { "Connectome Feed Management" })
    public ResponseEntity<?> handleMemo(@RequestParam("feedId") String feedId, @RequestParam("status") Integer status) {
        try {
            boolean result = iConnectomeFeedService.handleMemo(feedId, status);
            if (!result) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Parameters");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/getFeedByDate/{connectomeId}")
    @Operation(summary = "Get feed recommend date equal or larger than param date", tags = { "Connectome Feed Management" })
    public ResponseEntity<?> getByConnectomeIdAndDate(
        @PathVariable("connectomeId") String connectomeId,
        @RequestParam("lang") String lang,
        @RequestParam("recommendDate") Instant recommendDate
    ) {
        List<Feed> feeds = iConnectomeFeedService.findFeedByConnectomeIdAndLangAndRecommendDate(connectomeId, lang, recommendDate);
        return ResponseEntity.ok(feeds);
    }

    @GetMapping("/countFeedByDate/{connectomeId}")
    @Operation(summary = "Count feed recommend date equal or larger than param date", tags = { "Connectome Feed Management" })
    public ResponseEntity<?> countFeedByDate(
        @PathVariable("connectomeId") String connectomeId,
        @RequestParam("recommendDate") Instant recommendDate
    ) {
        Long feedCount = iConnectomeFeedService.countFeedByConnectomeIdAndRecommendDate(connectomeId, recommendDate);
        return ResponseEntity.ok(feedCount);
    }

    //
    @PostMapping("/getActivity/{connectomeId}")
    @Operation(summary = "getActivity", tags = { "Connectome Feed Management" })
    public ResponseEntity<?> getActivity(
        @PathVariable(value = "connectomeId") String connectomeId,
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam("orderBy") String orderBy,
        @RequestParam("sortDirection") String sortDirection,
        @RequestParam(value = "lang", required = false) String lang,
        @RequestBody List<FilterFeedDTO> filterFeedDTOS
    ) {
        List<ActivityDTO> activityDTOs = new ArrayList<>();
        try {
            Page<Feed> feedsPage = iConnectomeFeedService.findFeedByConnectomeIdAndActivity(
                connectomeId,
                filterFeedDTOS.get(0),
                lang,
                page,
                size,
                orderBy,
                sortDirection
            );

            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            activityDTOs =
                feedsPage
                    .getContent()
                    .stream()
                    .map(
                        feed -> {
                            ActivityDTO activityDTO = modelMapper.map(feed, ActivityDTO.class);
                            activityDTO.setCollectionType(Constants.page.FEED.type);
                            return activityDTO;
                        }
                    )
                    .collect(Collectors.toList());

            Page<PersonalDocument> personalDocumentsPage = ipersonalDocumentService.getPersonalDocumentByConnectomeIdAndActivity(
                connectomeId,
                filterFeedDTOS.get(0),
                lang,
                page,
                size,
                orderBy,
                sortDirection
            );
            for (PersonalDocument personalDocument : personalDocumentsPage.getContent()) {
                ActivityDTO activityDTO = modelMapper.map(personalDocument, ActivityDTO.class);
                activityDTO.setCollectionType(Constants.page.PERSONAL_DOCUMENT.type);
                activityDTOs.add(activityDTO);
            }
            // Page<ActivityDTO> activityDTOsPage = new PageImpl<>(activityDTOs, PageRequest.of(page, size * 2), feedsPage.getTotalElements() + personalDocumentsPage.getTotalElements());
            ActivityResponse activityResponse = new ActivityResponse();
            activityResponse.setActivityList(activityDTOs);
            activityResponse.setTotalItems(feedsPage.getTotalElements() + personalDocumentsPage.getTotalElements());
            return ResponseEntity.ok(activityResponse);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
