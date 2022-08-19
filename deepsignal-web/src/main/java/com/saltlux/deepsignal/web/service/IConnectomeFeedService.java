package com.saltlux.deepsignal.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.saltlux.deepsignal.web.service.dto.ConnectomeFeedDTO;

public interface IConnectomeFeedService {
    ConnectomeFeedDTO getFeedConnectomeByDocId(String docId) throws JsonProcessingException;

    long countFeedByConnectome(String connectomeId);
}
