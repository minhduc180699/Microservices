package com.saltlux.deepsignal.web.util;

import com.saltlux.deepsignal.web.api.errors.BadRequestException;
import com.saltlux.deepsignal.web.api.errors.ErrorConstants;
import com.saltlux.deepsignal.web.service.dto.FilterFeedDTO;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.zalando.problem.Status;

@Component
public class ConnectAdapterApi {

    @Value("${application.external-api.deepsignal-adapter}")
    private String externalApi;

    @Autowired
    private RestTemplate restTemplate;

    public String getDataFromAdapterApi(String uri, Map<String, Object> params, HttpMethod method) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (params != null) {
            Set set = params.keySet();
            for (Object key : set) {
                builder.queryParam((String) key, params.get(key));
            }
        }
        builder.encode(Charset.forName("UTF-8"));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(
            builder.toUriString(),
            method,
            entity,
            new ParameterizedTypeReference<String>() {}
        );
        if (!result.getStatusCode().equals(HttpStatus.OK)) {
            throw new BadRequestException(ErrorConstants.DEFAULT_TYPE, null, Status.valueOf(result.getStatusCodeValue()));
        }
        return result.getBody();
    }

    public String getDataFromAdapterApi(String uri, Map<String, Object> params, HttpMethod method, Map<String, Object> uriParam) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (params != null) {
            Set set = params.keySet();
            for (Object key : set) {
                builder.queryParam((String) key, params.get(key));
            }
        }
        builder.encode(Charset.forName("UTF-8"));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(
            builder.toUriString(),
            method,
            entity,
            new ParameterizedTypeReference<String>() {},
            uriParam
        );
        if (!result.getStatusCode().equals(HttpStatus.OK)) {
            throw new BadRequestException(ErrorConstants.DEFAULT_TYPE, null, Status.valueOf(result.getStatusCodeValue()));
        }
        return result.getBody();
    }

    public String getDataFromAdapterApi(
        String uri,
        Map<String, Object> params,
        HttpMethod method,
        Map<String, Object> uriParam,
        List<?> filterFeedDTOS
    ) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (params != null) {
            Set set = params.keySet();
            for (Object key : set) {
                builder.queryParam((String) key, params.get(key));
            }
        }
        builder.encode(StandardCharsets.UTF_8);
        HttpEntity<?> entity = new HttpEntity<>(filterFeedDTOS, headers);
        ResponseEntity<String> result = null != uriParam
            ? restTemplate.exchange(builder.toUriString(), method, entity, new ParameterizedTypeReference<String>() {}, uriParam)
            : restTemplate.exchange(builder.toUriString(), method, entity, new ParameterizedTypeReference<String>() {});
        if (!result.getStatusCode().equals(HttpStatus.OK)) {
            throw new BadRequestException(ErrorConstants.DEFAULT_TYPE, null, Status.valueOf(result.getStatusCodeValue()));
        }
        return result.getBody();
    }

    public String getExternalApi() {
        return externalApi;
    }

    public void setExternalApi(String externalApi) {
        this.externalApi = externalApi;
    }
}
