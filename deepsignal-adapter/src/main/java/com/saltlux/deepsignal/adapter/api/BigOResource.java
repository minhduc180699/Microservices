package com.saltlux.deepsignal.adapter.api;

import com.saltlux.bigo.restapi.client.dto.SearchResult;
import com.saltlux.bigo.restapi.client.dto.TimeSeriesResult;
import com.saltlux.deepsignal.adapter.service.BigOService;
import com.saltlux.deepsignal.adapter.service.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bigo")
@Tag(name = "BigO Content Service", description = "User Service API")
public class BigOResource {

    private BigOService bigOService;

    public BigOResource(BigOService bigOService) {
        this.bigOService = bigOService;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //    @PostMapping(value = "/feed/next")
    //    @Operation(summary = "Returns a list of User Feeds - (Feeds)", tags = { "Post Content Service" })
    //    public FeederResult postNext(@Valid @RequestBody FeederRequest request) {
    //    	return new ListFeed().readFeedNext(request);
    //    }
    //
    //    @PostMapping(value = "/entity/next")
    //    @Operation(summary = "Returns a list of User Feeds - (Entity)", tags = { "Post Content Service" })
    //    public FeederResult entityNext(@Valid @RequestBody FeederRequest request) {
    //    	return new ListFeed().readFeedNext(request);
    //    }
    //
    //    @PostMapping(value = "/signal/next")
    //    @Operation(summary = "Returns a list of User Feeds - (Signal)", tags = { "Post Content Service" })
    //    public FeederResult signalNext(@Valid @RequestBody FeederRequest request) {
    //    	return new ListFeed().readFeedNext(request);
    //    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @PostMapping(value = "/search")
    @Operation(summary = "Returns a list of User Feeds - (Details)", tags = { "BigO Content Service" })
    public SearchResult getSearchResults(@Valid @RequestBody BigOScrollRequest scrollRequest) throws Exception {
        if ("".equals(scrollRequest.getScrollId()) || scrollRequest.getScrollId() == null) {
            return bigOService.getSearchResult(scrollRequest);
        } else {
            return bigOService.getSearchNextResult(scrollRequest);
        }
    }

    @PostMapping(value = "/detail")
    @Operation(summary = "Returns a list of User Feeds - (Details)", tags = { "BigO Content Service" })
    public PostDetailResult getPostContent(@Valid @RequestBody PostDetailRequest request) throws Exception {
        return bigOService.getPostDetail(request);
    }

    @PostMapping(value = "/search/timeseries")
    @Operation(summary = "Returns a list of TimeSeries", tags = { "BigO Content Service" })
    public List<BigOTimeSeriesResult> getSearchTimeSeriesResults(@Valid @RequestBody BigOTimeSeriesRequest seriesRequest) throws Exception {
        return bigOService.getSearchTimeSeries(seriesRequest);
    }
}
