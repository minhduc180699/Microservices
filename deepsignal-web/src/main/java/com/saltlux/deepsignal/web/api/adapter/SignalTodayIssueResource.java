package com.saltlux.deepsignal.web.api.adapter;

import com.saltlux.deepsignal.web.api.vm.TimeSeriesParams;
import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.util.ConnectAdapterApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/signal-issue")
@Tag(name = "Signal Today Issue Management", description = "The signal today issue management API")
public class SignalTodayIssueResource {

    private ConnectAdapterApi connectAdapterApi;

    private final RestTemplate restTemplate;

    private final ApplicationProperties applicationProperties;

    public SignalTodayIssueResource(
        ConnectAdapterApi connectAdapterApi,
        RestTemplate restTemplate,
        ApplicationProperties applicationProperties
    ) {
        this.connectAdapterApi = connectAdapterApi;
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }

    @GetMapping("/paging/{connectomeId}")
    @Operation(summary = "Get Signal Today Issue By Paging", tags = { "Signal Today Issue Management" })
    public ResponseEntity<?> paging(
        Pageable pageable,
        @PathVariable String connectomeId,
        @RequestParam("work_day") String workDay,
        @RequestParam("signalType") String signalType
    ) {
        try {
            String uri = connectAdapterApi.getExternalApi() + "/signal/paging/" + connectomeId;
            Map<String, Object> params = new HashMap<>();
            params.put("page", pageable.getPageNumber());
            params.put("size", pageable.getPageSize());
            String sort = "";
            for (Sort.Order order : pageable.getSort()) {
                sort = order.getProperty() + "," + order.getDirection();
            }
            params.put("sort", StringUtils.isNotBlank(sort) ? sort : "displayOrder,DESC");
            params.put("work_day", workDay);
            params.put("signalType", signalType);
            return new ResponseEntity<>(connectAdapterApi.getDataFromAdapterApi(uri, params, HttpMethod.GET), HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/time-series")
    @Operation(summary = "Get Signal Time Series data", tags = { "Signal Today Issue Management" })
    public ResponseEntity<?> timeSeries(@Valid @RequestBody TimeSeriesParams seriesParams) {
        try {
            if (StringUtils.isEmpty(seriesParams.getCompkeywords())) {
                return ResponseEntity.badRequest().body("compkeywords are null - 101");
            }
            StringBuilder urlSearch = new StringBuilder(applicationProperties.getExternalApi().getDeepsignalTrendTimeseries());
            urlSearch.append(Constants.TIME_SERIES);
            String uriParams = UriComponentsBuilder
                .fromHttpUrl(urlSearch.toString())
                .queryParam("connectome_id", seriesParams.getConnectome_id())
                .queryParam("compkeywords", seriesParams.getCompkeywords())
                .queryParam("country", seriesParams.getCountry())
                .queryParam("search_source", seriesParams.getSearch_source())
                .queryParam("from", seriesParams.getFrom())
                .queryParam("until", seriesParams.getUntil())
                .queryParam("request_id", seriesParams.getRequest_id())
                .build()
                .toUriString();

            return ResponseEntity.ok().body(restTemplate.getForEntity(uriParams, String.class).getBody());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
