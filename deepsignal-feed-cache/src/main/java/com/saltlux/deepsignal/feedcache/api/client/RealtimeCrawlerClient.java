package com.saltlux.deepsignal.feedcache.api.client;

import com.saltlux.deepsignal.feedcache.model.FeedRealtimeCrawlerModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "realtime-crawler", url = "http://localhost:8089")
public interface RealtimeCrawlerClient {
    @PostMapping("/deepsignalconverter/documentconverter/urlconvert")
    ResponseEntity<String> postRealtimeCrawler(@RequestBody List<FeedRealtimeCrawlerModel> feedRealtimeCrawlerModelList);
}
