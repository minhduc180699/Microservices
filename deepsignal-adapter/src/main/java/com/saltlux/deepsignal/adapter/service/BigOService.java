package com.saltlux.deepsignal.adapter.service;

import com.saltlux.bigo.restapi.client.dto.SearchRequest;
import com.saltlux.bigo.restapi.client.dto.SearchResult;
import com.saltlux.bigo.restapi.client.dto.TimeSeriesRequest;
import com.saltlux.bigo.restapi.client.dto.TimeSeriesResult;
import com.saltlux.bigo.restapi.client.dto.aggation.DateAggregation;
import com.saltlux.bigo.restapi.client.dto.filter.DateRange;
import com.saltlux.bigo.restapi.client.dto.query.SyntaxQuery;
import com.saltlux.bigo.restapi.client.sort.AbstractSort;
import com.saltlux.bigo.restapi.client.sort.FieldSort;
import com.saltlux.bigo.restapi.client.sort.ScoreSort;
import com.saltlux.bigo.types.Language;
import com.saltlux.bigo.types.Order;
import com.saltlux.deepsignal.adapter.config.ApplicationProperties;
import com.saltlux.deepsignal.adapter.config.Constants;
import com.saltlux.deepsignal.adapter.service.dto.*;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.*;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BigOService {

    private final Logger log = LoggerFactory.getLogger(BigOService.class);

    RestTemplate restTemplate;

    ApplicationProperties applicationProperties;

    public BigOService(RestTemplate restTemplate, ApplicationProperties applicationProperties) {
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }

    private String execBigoRequest(final String apiURL, String paramJSON) {
        try {
            return restTemplate.postForObject(apiURL, paramJSON, String.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private <T> T _call_bigo(final String API_URL, Object request, Class<T> response_class) {
        //caustion) must use gson of bigo. for generic type.
        final com.google.gson.Gson gson = com.saltlux.bigo.restapi.client.gson.GsonTools.getGson();

        final String request_json = gson.toJson(new com.saltlux.bigo.vertx.restapi.client.Request("access_key", "request_id", request));

        final String response_json = execBigoRequest(API_URL, request_json);

        final com.google.gson.JsonObject jsonObject = gson.fromJson(response_json, com.google.gson.JsonObject.class);

        com.google.gson.JsonPrimitive jsonPrimitive;
        final int result = jsonObject.getAsJsonPrimitive("result").getAsInt();

        jsonPrimitive = jsonObject.getAsJsonPrimitive("reason");
        final String reason = (jsonPrimitive != null) ? jsonPrimitive.getAsString() : null;

        jsonPrimitive = jsonObject.getAsJsonPrimitive("return_type");
        final String className = (jsonPrimitive != null) ? jsonPrimitive.getAsString() : null;
        final com.google.gson.JsonElement returnJson = jsonObject.get("return_object");

        final Response response = new Response(null, result, reason, className, returnJson, gson); //대기하는 Thread를 진행

        if (response.succeeded() == false) throw new IllegalArgumentException(response.errorReason());

        return response.getReturn(response_class);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public PostDetailResult getPostDetail(@Valid PostDetailRequest request) throws Exception {
        //1. get where is data stored?
        final com.saltlux.bigo.restapi.client.dto.FindRepositoryResult bigo_result1 = _call_bigo(
            this.applicationProperties.getExternalApi().getBigoAPI() + Constants.BIGO_REPOSITORY,
            new com.saltlux.bigo.restapi.client.dto.FindRepositoryRequest(request.post_id),
            com.saltlux.bigo.restapi.client.dto.FindRepositoryResult.class
        );

        if (bigo_result1.repositories == null || bigo_result1.repositories.length <= 0) {
            throw new Exception("Can't not found repository : " + request.post_id);
            //return null;
        }

        /////////////////////////////////////////////////
        //2. read detail data
        final com.saltlux.bigo.restapi.client.dto.SearchRequest bigo_request = new com.saltlux.bigo.restapi.client.dto.SearchRequest();

        bigo_request.setIndexes(bigo_result1.repositories[0].index);
        bigo_request.setQuery(new com.saltlux.bigo.restapi.client.dto.query.IdsQuery(request.post_id));
        bigo_request.setFields(
            FieldIndex.title,
            FieldIndex.content,
            FieldIndex.published_at,
            FieldIndex.writer,
            FieldIndex.images,
            FieldIndex.tms_ne_person,
            FieldIndex.tms_ne_location,
            FieldIndex.tms_ne_organization,
            FieldIndex.tms_sentiment_polarity_score,
            FieldIndex.project_id
        );

        final com.saltlux.bigo.restapi.client.dto.SearchResult bigo_result2 = _call_bigo(
            this.applicationProperties.getExternalApi().getBigoAPI() + Constants.BIGO_SEARCH,
            bigo_request,
            com.saltlux.bigo.restapi.client.dto.SearchResult.class
        );

        if (bigo_result2.total_hits <= 0) return null;
        if (bigo_result2.getDocuments().size() <= 0) return null;

        final com.saltlux.bigo.restapi.client.dto.SearchResult.Document doc = bigo_result2.getDocuments().get(0);

        return new PostDetailResult(request.connectome_id, request.post_id, request.post_type, doc);
    }

    public SearchResult getSearchResult(BigOScrollRequest scrollRequest) throws Exception {
        /////////////////////////////////////////////////
        // read detail data
        final SearchRequest bigoRequest = new SearchRequest();
        AbstractSort sorts[] = { new FieldSort("published_at", Order.DESC), new ScoreSort("order", Order.DESC) };
        bigoRequest.setIndexes(Constants.BIGO_INDEXES);
        bigoRequest.setQuery(new SyntaxQuery(scrollRequest.getQuery()));
        bigoRequest.setSort(sorts);
        bigoRequest.setLanguage(Language.convertOf(scrollRequest.getLanguage()));

        bigoRequest.setScroll(550);
        bigoRequest.setFields(
            FieldIndex.title,
            FieldIndex.content,
            FieldIndex.published_at,
            FieldIndex.writer,
            FieldIndex.images,
            FieldIndex.project_id
        );

        final SearchResult searchResult = _call_bigo(
            this.applicationProperties.getExternalApi().getBigoAPI() + Constants.BIGO_SEARCH,
            bigoRequest,
            SearchResult.class
        );

        if (searchResult.total_hits <= 0) return null;
        if (searchResult.getDocuments().size() <= 0) return null;

        return searchResult;
    }

    public SearchResult getSearchNextResult(BigOScrollRequest scrollRequest) {
        final SearchRequest bigoRequest = new SearchRequest();
        bigoRequest.setScroll(scrollRequest.getScrollId(), 550);
        bigoRequest.setFields(
            FieldIndex.title,
            FieldIndex.content,
            FieldIndex.published_at,
            FieldIndex.writer,
            FieldIndex.images,
            FieldIndex.project_id
        );

        final SearchResult searchResult = _call_bigo(
            this.applicationProperties.getExternalApi().getBigoAPI() + Constants.BIGO_SEARCH_NEXT,
            bigoRequest,
            SearchResult.class
        );

        if (searchResult.total_hits <= 0) return null;
        if (searchResult.getDocuments().size() <= 0) return null;

        return searchResult;
    }

    public List<BigOTimeSeriesResult> getSearchTimeSeries(BigOTimeSeriesRequest seriesRequest) {
        final DateAggregation dateAggregation = new DateAggregation(seriesRequest.getInterval(), seriesRequest.getTimezone());

        List<BigOTimeSeriesResult> bigOTimeSeriesResults = new ArrayList<>();

        TimeSeriesRequest timeSeriesRequest = new TimeSeriesRequest(dateAggregation, false, false);
        timeSeriesRequest.setIndexes(Constants.BIGO_INDEXES);
        timeSeriesRequest.setLanguage(Language.convertOf(seriesRequest.getLanguage()));

        if (seriesRequest.getFrom() == null) {
            Calendar cal = Calendar.getInstance();
            // Get data from one year ago
            cal.add(Calendar.YEAR, -1);
            seriesRequest.setFrom(cal.getTime());
        }

        if (seriesRequest.getUntil() == null) {
            seriesRequest.setUntil(new Date());
        }

        DateRange dateRange = new DateRange(seriesRequest.getFrom(), seriesRequest.getUntil());
        timeSeriesRequest.setPublishedAt(dateRange);

        log.info("Starting get time series data from {} to {}", seriesRequest.getFrom(), seriesRequest.getUntil());

        if ("".equals(seriesRequest.getQuery()) || seriesRequest.getQuery() == null) return null;

        String[] keywords = seriesRequest.getQuery().split(",");

        for (int i = 0; i < keywords.length; i++) {
            timeSeriesRequest.setQuery(new SyntaxQuery(keywords[i]));

            final TimeSeriesResult timeSeriesResult = _call_bigo(
                this.applicationProperties.getExternalApi().getBigoAPI() + Constants.BIGO_SEARCH_TIMESERIES,
                timeSeriesRequest,
                TimeSeriesResult.class
            );
            BigOTimeSeriesResult result = new BigOTimeSeriesResult(keywords[i], timeSeriesResult.retrieve);

            if (timeSeriesResult.total_hits > 0) {
                bigOTimeSeriesResults.add(result);
            }
        }

        return bigOTimeSeriesResults;
    }
}
