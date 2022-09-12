package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.UserActivityLog;
import com.saltlux.deepsignal.web.service.dto.TrainingDocumentInfoDTO;
import java.util.List;

public interface IUserActivityLogService {
    public UserActivityLog saveUserActivityLog(UserActivityLog userActivityLog);

    public List<UserActivityLog> saveAllUserActivityLogs(List<UserActivityLog> listUserActivityLog);

    UserActivityLog saveUserActivityLogWithBase(UserActivityLog userActivityLog);
    UserActivityLog saveUserActivityLogWithBase();

    List<TrainingDocumentInfoDTO> getTrainingDocumentsByKeyword(
        String connectomeId,
        String keyword,
        String dateFrom,
        String dateTo,
        Integer hour
    );

    List<TrainingDocumentInfoDTO> getTrainingDocumentsByType(
        String connectomeId,
        String type,
        String dateFrom,
        String dateTo,
        Integer hour
    );
}
