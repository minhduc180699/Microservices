package com.saltlux.deepsignal.feedcache.api.client;

import com.saltlux.deepsignal.feedcache.model.FeedRealtimeCrawlerModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RealtimeCrawlerClientFallback implements RealtimeCrawlerClient{
    @Override
    public ResponseEntity<String> postRealtimeCrawler(List<FeedRealtimeCrawlerModel> feedRealtimeCrawlerModelList) {
        return null;
    }
}
