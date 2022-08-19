package com.saltlux.deepsignal.adapter.service;

import com.saltlux.deepsignal.adapter.domain.WebSource;
import com.saltlux.deepsignal.adapter.service.dto.WebSourceConditionDTO;
import java.util.List;

public interface IWebSourceService extends GeneralService<WebSource> {
    List<WebSource> createWebSource(WebSource webSource);

    WebSourceConditionDTO addWebSourceCondition(WebSourceConditionDTO webSourceDTO);

    List<WebSource> updateWebSourceCondition(WebSourceConditionDTO webSourceDTO);
}
