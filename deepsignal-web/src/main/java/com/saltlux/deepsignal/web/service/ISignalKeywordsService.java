package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.SignalKeywords;
import com.saltlux.deepsignal.web.service.dto.SignalKeywordsDTO;
import java.util.List;

public interface ISignalKeywordsService extends GeneralService<SignalKeywords, Long> {
    SignalKeywords save(SignalKeywordsDTO signalKeywordsDTO, String connectomeId);

    List<SignalKeywords> findSignalKeywordsByConnectomeIdAndStatus(String connectomeId, Integer status);

    void deleteByConnectomeIdAndId(String id, Long signalId);
}
