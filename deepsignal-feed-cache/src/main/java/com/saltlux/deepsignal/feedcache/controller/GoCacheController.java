package com.saltlux.deepsignal.feedcache.controller;

import com.saltlux.deepsignal.feedcache.config.Appconfig;
import com.saltlux.deepsignal.feedcache.model.DocCreateModel;
import com.saltlux.deepsignal.feedcache.model.DocDataModel;
import com.saltlux.deepsignal.feedcache.model.FeedInputModel;
import com.saltlux.deepsignal.feedcache.model.FeedUpdateModel;
import com.saltlux.deepsignal.feedcache.model.request.RequestBodyGetDocument;
import com.saltlux.deepsignal.feedcache.model.request.RequestBodyGetListDoc;
import com.saltlux.deepsignal.feedcache.service.IFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RefreshScope
@RestController
@RequestMapping("/goCache")
public class GoCacheController {
    @Autowired
    private Appconfig appconfig;
    @Qualifier("feedService")
    @Autowired
    private IFeedService iFeedService;
    @GetMapping("/getListDoc")
    private ResponseEntity<?> getListFeed(@RequestParam(value = "connectomeId", required = true) String connectomeId,
                                          @RequestParam(value = "requestId", required = false) String request_id,
                                          @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                          @RequestParam(value = "size", required = false, defaultValue = "10") Integer size){
        return ResponseEntity.ok(iFeedService.getListFeed(connectomeId, request_id, page, size));
    }

    @GetMapping("/getListFilterDoc")
    private ResponseEntity<?> getListFilterFeed(@RequestParam(value = "connectomeId", required = true) String connectomeId,
                                          @RequestParam(value = "requestId", required = false) String request_id,
                                          @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                          @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                                @RequestParam(value = "type", required = false) String type){
        return ResponseEntity.ok(iFeedService.getListFilterFeed(connectomeId, request_id, page, size, type));
    }
    @PostMapping("/getListDocumentByIds")
    private ResponseEntity<?> getListDocumentByIds(@RequestBody RequestBodyGetListDoc requestBody){
        return ResponseEntity.ok(iFeedService.getListDocumentByIds(requestBody));
    }
    @GetMapping("/searchDoc")
    private ResponseEntity<?> searchFeed(@RequestParam(value = "connectomeId", required = true) String connectomeId,
                                          @RequestParam(value = "requestId", required = false) String request_id,
                                          @RequestParam(value = "keyword", required = false) String keyword,
                                          @RequestParam(value = "from", required = false) String from,
                                          @RequestParam(value = "until", required = false) String until,
                                          @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                          @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                          @RequestParam(value = "search_type", required = false) String searchType,
                                          @RequestParam(value = "channels", required = false) String channels,
                                          @RequestParam(value = "type", required = false) String type,
                                          @RequestParam(value = "lang", required = true) String lang){
        return ResponseEntity.ok(iFeedService.searchFeed(connectomeId, request_id, keyword, from, until, page, size, searchType, channels, lang, type));
    }
    @GetMapping("/getDoc")
    private ResponseEntity<?> getFeed(@RequestParam(value = "connectomeId") String connectomeId,
                                      @RequestParam(value = "requestId",required = false) String request_id,
                                      @RequestParam(value = "docId") String docId){
        return ResponseEntity.ok(iFeedService.getFeed(connectomeId, request_id, docId));
    }
    @PutMapping("/updateDoc")
    private ResponseEntity<?> updateFeed(@RequestBody FeedUpdateModel data){
        return ResponseEntity.ok(iFeedService.updateFeed(data));
    }
    @PostMapping("/createDoc")
    private ResponseEntity<?> createFeed(@RequestBody DocCreateModel data){
         return ResponseEntity.ok(iFeedService.createFeed(data));
    }
    @PutMapping("/likeDoc")
    private ResponseEntity<?> likeFeed(@RequestBody FeedInputModel feedModel){
        return ResponseEntity.ok(iFeedService.likeFeed(feedModel));
    }
    @PutMapping("/deleteDoc")
    private ResponseEntity<?> hideFeed(@RequestBody FeedInputModel feedModel){
        return ResponseEntity.ok(iFeedService.hideFeed(feedModel));
    }
    @PutMapping("/bookmarkDoc")
    private ResponseEntity<?> bookmarkFeed(@RequestBody FeedInputModel feedModel){
        return ResponseEntity.ok(iFeedService.bookmarkFeed(feedModel));
    }

    @PostMapping("/getDocumentById")
    public ResponseEntity<?> getDocumentById(@RequestBody RequestBodyGetDocument requestBody){
        return ResponseEntity.ok(iFeedService.getDocumentById(requestBody));
    }
}
