package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.InteractionUser;
import com.saltlux.deepsignal.web.service.dto.InteractionStatisticDTO;

public interface IInteractionUserService {
    InteractionUser save(String feedId, int typeInteraction) throws Exception;

    InteractionStatisticDTO statisticInteraction(String feedId) throws Exception;

    boolean delete(String feedId, int typeInteraction) throws Exception;

    void updateType();
}
