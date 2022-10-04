package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.ChromeBookmarkTrained;
import com.saltlux.deepsignal.web.domain.UserSetting;
import java.util.List;

public interface IChromeBookmarkTrainedService {
    List<ChromeBookmarkTrained> findAllByConnectomeId(String userId);
    List<ChromeBookmarkTrained> save(List<ChromeBookmarkTrained> lstBookmarkTrained, String connectomeId) throws Exception;
}
