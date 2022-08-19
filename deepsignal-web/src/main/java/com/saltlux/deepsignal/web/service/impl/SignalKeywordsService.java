package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.domain.SignalKeywords;
import com.saltlux.deepsignal.web.repository.SignalKeywordsRepository;
import com.saltlux.deepsignal.web.service.ISignalKeywordsService;
import com.saltlux.deepsignal.web.service.dto.SignalKeywordsDTO;
import com.saltlux.deepsignal.web.util.ConnectAdapterApi;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@Log4j2
public class SignalKeywordsService implements ISignalKeywordsService {

    private final SignalKeywordsRepository signalKeywordsRepository;

    private final String issueTrackingService;

    private ConnectAdapterApi connectAdapterApi;

    public SignalKeywordsService(
        SignalKeywordsRepository signalKeywordsRepository,
        ApplicationProperties applicationProperties,
        ConnectAdapterApi connectAdapterApi
    ) {
        this.signalKeywordsRepository = signalKeywordsRepository;
        this.issueTrackingService = applicationProperties.getExternalApi().getDeepsignalIssueTracking();
        this.connectAdapterApi = connectAdapterApi;
    }

    @Override
    public List<SignalKeywords> findAll() {
        return signalKeywordsRepository.findAll();
    }

    @Override
    public Optional<SignalKeywords> findById(Long id) {
        return signalKeywordsRepository.findById(id);
    }

    @Override
    public SignalKeywords save(SignalKeywordsDTO signalKeywordsDTO, String connectomeId) {
        SignalKeywords signalKeywords;
        Boolean isNew = false;
        if (!Objects.isNull(signalKeywordsDTO.getId())) {
            if (signalKeywordsRepository.findById(signalKeywordsDTO.getId()).isPresent()) {
                signalKeywords = signalKeywordsRepository.findById(signalKeywordsDTO.getId()).get();
                signalKeywords.setId(signalKeywordsDTO.getId());
            } else {
                signalKeywords = new SignalKeywords();
                signalKeywords.setLanguage(signalKeywordsDTO.getLanguage());
                isNew = true;
            }
        } else {
            signalKeywords = new SignalKeywords();
            signalKeywords.setLanguage(signalKeywordsDTO.getLanguage());
            isNew = true;
        }
        if (signalKeywordsDTO.getCreatedDate() != null && isNew == true) signalKeywords.setCreatedDate(signalKeywordsDTO.getCreatedDate());
        if (signalKeywordsDTO.getStatus() != null) signalKeywords.setStatus(signalKeywordsDTO.getStatus());
        signalKeywords.setKeywords(signalKeywordsDTO.getKeywords());
        signalKeywords.setMainKeyword(signalKeywordsDTO.getMainKeyword());
        signalKeywords.setType(Constants.SignalIssues.IT.toString());
        signalKeywords.setConnectome(new Connectome(connectomeId));

        SignalKeywords snKeywords = signalKeywordsRepository.save(signalKeywords);

        if (snKeywords != null) {
            StringBuilder issueTrackingAPI = new StringBuilder(issueTrackingService + "/doMetaSearchForIssueTracking/");
            issueTrackingAPI.append("?IssueTrackingId=" + snKeywords.getId());
            issueTrackingAPI.append("&connectomeId=" + connectomeId);
            issueTrackingAPI.append("&Term=d1");
            issueTrackingAPI.append("&keywords=" + snKeywords.getKeywords());
            issueTrackingAPI.append("&mainKeyword=" + snKeywords.getMainKeyword());
            issueTrackingAPI.append("&Language=" + snKeywords.getLanguage());
            RestTemplate restTemplate = new RestTemplate();

            restTemplate.postForLocation(issueTrackingAPI.toString(), null);
        }

        return snKeywords;
    }

    @Override
    public List<SignalKeywords> findSignalKeywordsByConnectomeIdAndStatus(String connectomeId, Integer status) {
        return signalKeywordsRepository.findSignalKeywordsByConnectome_ConnectomeIdAndStatus(connectomeId, status);
    }

    @Transactional
    @Override
    public void deleteByConnectomeIdAndId(String id, Long signalId) {
        Optional<SignalKeywords> signalKeyword = signalKeywordsRepository.findById(signalId);
        if (signalKeyword.isPresent()) {
            signalKeyword.get().setStatus(-1);
            signalKeywordsRepository.save(signalKeyword.get());
        }
        String uri = connectAdapterApi.getExternalApi() + "/signal/delete";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        connectAdapterApi.getDataFromAdapterApi(uri, params, HttpMethod.DELETE);
    }

    @Override
    public SignalKeywords save(SignalKeywords signalKeywords) {
        return signalKeywordsRepository.save(signalKeywords);
    }

    @Transactional
    @Override
    public void remove(Long id) {
        signalKeywordsRepository.deleteById(id);
    }
}
