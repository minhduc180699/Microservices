package com.saltlux.deepsignal.web.api.client;

import com.saltlux.deepsignal.web.service.dto.Feed;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class DsFeedClientFallback implements DsFeedClient {

    @Override
    public ResponseEntity<?> searchFeed(
        String connectomeId,
        String request_id,
        String keyword,
        String from,
        String until,
        Integer page,
        Integer size,
        String searchType,
        String channels,
        String type,
        String lang
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("connectomeFeeds", new ArrayList<Feed>());
        response.put("currentPage", 0);
        response.put("totalItems", 0);
        response.put("totalPages", 0);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getFeed(String connectomeId, String requestId, String docId) {
        Map<String, Object> response = new HashMap<>();
        response.put("Card", -1);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getListDocumentByIds(JSONObject body) {
        Map<String, Object> response = new HashMap<>();
        response.put("connectomeFeeds", new ArrayList<Feed>());
        response.put("currentPage", 0);
        response.put("totalItems", 0);
        response.put("totalPages", 0);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> handleActivityLike(JSONObject bodyJSON) {
        Map<String, Object> response = new HashMap<>();
        response.put("requestId", -1);
        response.put("connectomeId", -1);
        response.put("docId", -1);
        response.put("liked", -1);
        response.put("isBookmarked", -1);
        response.put("isDeleted", -1);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> handleActivityBookmark(JSONObject bodyJSON) {
        Map<String, Object> response = new HashMap<>();
        response.put("requestId", -1);
        response.put("connectomeId", -1);
        response.put("docId", -1);
        response.put("liked", -1);
        response.put("isBookmarked", -1);
        response.put("isDeleted", -1);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> handleHideDoc(JSONObject bodyJSON) {
        Map<String, Object> response = new HashMap<>();
        response.put("requestId", -1);
        response.put("connectomeId", -1);
        response.put("docId", -1);
        response.put("liked", -1);
        response.put("isBookmarked", -1);
        response.put("isDeleted", -1);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
