package com.saltlux.deepsignal.web.api.external;

import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.util.ConnectAdapterApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/related-content")
@Tag(name = "Related Content", description = "The related content API")
public class RelatedContentController {

    private final String URL_RELATED_CONTENT;
    private ConnectAdapterApi connectAdapterApi;

    public RelatedContentController(ApplicationProperties applicationProperties, ConnectAdapterApi connectAdapterApi) {
        this.URL_RELATED_CONTENT = applicationProperties.getExternalApi().getDeepsignalRelatedContent();
        this.connectAdapterApi = connectAdapterApi;
    }

    @GetMapping("/docs")
    @Operation(summary = "Get Related Docs In The Internal Search By Feed Doc", tags = { "Related Content" })
    public ResponseEntity<?> getDocRelated(
        @RequestParam(value = "connectomeId") String connectomeId,
        @RequestParam(value = "feedDocId") String feedDocId
    ) {
        String uri = URL_RELATED_CONTENT + "/getRelatedDocsInTheInternalSearchByFeedDoc";
        Map<String, Object> params = new HashMap<>();
        params.put("connectomeId", connectomeId);
        params.put("feedDocId", feedDocId);
        return new ResponseEntity<>(connectAdapterApi.getDataFromAdapterApi(uri, params, HttpMethod.GET), HttpStatus.OK);
    }
}
