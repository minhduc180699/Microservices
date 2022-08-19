package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.WebSource;
import com.saltlux.deepsignal.web.service.dto.WebSourceConditionDTO;
import java.util.List;

public interface IWebSourceService extends GeneralService<WebSource, String> {
    WebSource createWebSource(WebSource webSource);

    List<WebSource> addWebSourceCondition(WebSourceConditionDTO webSourceDTO);
}
