package com.saltlux.deepsignal.web.api.client;

import com.saltlux.deepsignal.web.service.dto.ConnectomeFeed;
import com.saltlux.deepsignal.web.service.dto.Feed;
import com.saltlux.deepsignal.web.service.dto.FilterFeedDTO;
import com.saltlux.deepsignal.web.service.dto.MetaSearchDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DsAdapterClientFallback implements DsAdapterClient{

    @Override
    public ResponseEntity<Map<String, Object>> getAllConnectomeFeed(int page, int size, String orderBy, String sortDirection) {
        return null;
    }

    @Override
    public ResponseEntity<ConnectomeFeed> getConnetomeFeedById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<?> getByIds(List<String> ids) {
        return null;
    }

    @Override
    public ResponseEntity<Map<String, Object>> getFeedByConnectomeId(int page, int size, String orderBy, String sortDirection, String keyword, String connectomeId, List<FilterFeedDTO> filterFeedDTOS) {
        Map<String, Object> response = new HashMap<>();
        response.put("connectomeFeeds", new ArrayList<Feed>());
        response.put("currentPage", 0);
        response.put("totalItems", 0);
        response.put("totalPages", 0);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, Object>> countFeedByConnectomeId(String connectomeId) {
        return null;
    }

    @Override
    public ResponseEntity<Map<String, Object>> getFeedByConnectomeIdAndTopic(int page, int size, String orderBy, String sortDirection, String connectomeId, String topic, boolean excepted) {
        Map<String, Object> response = new HashMap<>();
        response.put("connectomeFeeds", new ArrayList<Feed>());
        response.put("currentPage", 0);
        response.put("totalItems", 0);
        response.put("totalPages", 0);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> handleActivity(String docId, boolean state, String activity, String connectomeId, String page, int isLiked) {
        return null;
    }

    @Override
    public ResponseEntity<?> handleSharing(String id, String platform) {
        return null;
    }

    @Override
    public ResponseEntity<?> getDetailCard(String id) {
        return null;
    }

    @Override
    public ResponseEntity<?> handleMemo(String feedId, Integer status) {
        return null;
    }

    @Override
    public ResponseEntity<?> getByConnectomeIdAndDate(String connectomeId, String lang, Instant recommendDate) {
        Map<String, Object> response = new HashMap<>();
        response.put("connectomeFeeds", new ArrayList<Feed>());
        response.put("currentPage", 0);
        response.put("totalItems", 0);
        response.put("totalPages", 0);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> countFeedByDate(String connectomeId, Instant recommendDate) {
        return null;
    }

    @Override
    public ResponseEntity<?> getActivity(String connectomeId, int page, int size, String orderBy, String sortDirection, String lang, List<FilterFeedDTO> filterFeedDTOS) {
        return null;
    }

    @Override
    public ResponseEntity<Map<String, Object>> getAllPeoples(int page, int size, String orderBy, String sortDirection) {
        return null;
    }

    @Override
    public ResponseEntity<?> searchStock(String search) {
        return null;
    }

    @Override
    public ResponseEntity<Map<String, Object>> postMetasearchData(MetaSearchDTO metaSearchDTO) {
        return null;
    }
}
