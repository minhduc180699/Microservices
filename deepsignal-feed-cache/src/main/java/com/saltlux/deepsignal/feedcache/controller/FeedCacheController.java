package com.saltlux.deepsignal.feedcache.controller;

import com.saltlux.deepsignal.feedcache.config.Appconfig;
import com.saltlux.deepsignal.feedcache.dto.FeedDto;
import com.saltlux.deepsignal.feedcache.model.FeedDataModel;
import com.saltlux.deepsignal.feedcache.model.FeedModel;
import com.saltlux.deepsignal.feedcache.service.IFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RefreshScope
@RestController
@RequestMapping("/deepsignalFeedCache")
public class FeedCacheController {
    @Autowired
    private Appconfig appconfig;
    @Qualifier("feedService")
    @Autowired
    private IFeedService iFeedService;
    @GetMapping("/getListFeed")
    private ResponseEntity<?> getListFeed(@RequestParam(value = "connectomeId", required = true) String connectomeId,
                                          @RequestParam(value = "requestId", required = false) String request_id,
                                          @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                          @RequestParam(value = "size", required = false, defaultValue = "10") Integer size){
        return ResponseEntity.ok(iFeedService.getListFeed(connectomeId, request_id, page, size));
    }
    @PostMapping("/getListDocumentByIds")
    private ResponseEntity<?> getListDocumentByIds(@RequestBody FeedDto feedDto){
        return ResponseEntity.ok(iFeedService.getListDocumentByIds(feedDto));
    }
    @GetMapping("/searchFeed")
    private ResponseEntity<?> searchFeed(@RequestParam(value = "connectomeId", required = true) String connectomeId,
                                          @RequestParam(value = "requestId", required = false) String request_id,
                                          @RequestParam(value = "keyword", required = false) String keyword,
                                          @RequestParam(value = "from", required = false) String from,
                                          @RequestParam(value = "until", required = false) String until,
                                          @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                          @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                          @RequestParam(value = "search_type", required = false) String searchType,
                                          @RequestParam(value = "channels", required = false) String channels,
                                          @RequestParam(value = "type", required = false) String type,
                                          @RequestParam(value = "lang", required = true) String lang){
        return ResponseEntity.ok(iFeedService.searchFeed(connectomeId, request_id, keyword, from, until, page, size, searchType, channels, lang, type));
    }
    @GetMapping("/getFeed")
    private ResponseEntity<?> getFeed(@RequestParam(value = "connectomeId") String connectomeId,
                                      @RequestParam(value = "requestId",required = false) String request_id,
                                      @RequestParam(value = "_id") String _id){
        return ResponseEntity.ok(iFeedService.getFeed(connectomeId, request_id, _id));
    }
    @PutMapping("/updateFeed")
    private ResponseEntity<?> updateFeed(@RequestBody FeedModel data){
        return ResponseEntity.ok(iFeedService.updateFeed(data));
    }
    @PostMapping("/createFeed")
    private ResponseEntity<?> createFeed(@RequestBody FeedDataModel data){
        return ResponseEntity.ok(iFeedService.createFeed(data));
    }
    @PutMapping("/likeFeed")
    private ResponseEntity<?> likeFeed(@RequestBody FeedModel feedModel){
        return ResponseEntity.ok(iFeedService.likeFeed(feedModel));
    }
    @PutMapping("/hideFeed")
    private ResponseEntity<?> hideFeed(@RequestBody FeedModel feedModel){
        return ResponseEntity.ok(iFeedService.hideFeed(feedModel));
    }
    @PutMapping("/bookmarkFeed")
    private ResponseEntity<?> bookmarkFeed(@RequestBody FeedModel feedModel){
        return ResponseEntity.ok(iFeedService.bookmarkFeed(feedModel));
    }
}
