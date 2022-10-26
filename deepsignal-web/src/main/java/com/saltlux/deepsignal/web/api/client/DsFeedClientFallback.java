package com.saltlux.deepsignal.web.api.client;

import com.saltlux.deepsignal.web.service.dto.Feed;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
}
