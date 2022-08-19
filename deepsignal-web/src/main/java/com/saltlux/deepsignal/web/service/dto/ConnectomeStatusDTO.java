package com.saltlux.deepsignal.web.service.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectomeStatusDTO {

    private String connectomeId;

    private String status;

    private String lang;

    private Date firstCreatedDate;

    private Date whenLearningStarted;

    private Date lastUpdatedAt;

    private int numberOfTimesConnectomeWasUpdated;

    private int numberOfDocuments;

    private int numberOfUniqueEntities;
}
