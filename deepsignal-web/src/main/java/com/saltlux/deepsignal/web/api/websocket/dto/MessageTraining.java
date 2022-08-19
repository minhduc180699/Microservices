package com.saltlux.deepsignal.web.api.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageTraining {

    private String connectomeId;
    private String status;
    private String firstCreatedDate;
    private String whenLearningStarted;
    private String lastUpdatedAt;
    private String numberOfTimesConnectomeWasUpdated;
    private String numberOfDocuments;
    private String numberOfUniqueEntities;
}
