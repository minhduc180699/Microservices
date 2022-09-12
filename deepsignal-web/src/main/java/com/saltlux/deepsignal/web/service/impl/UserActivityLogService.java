package com.saltlux.deepsignal.web.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.UserActivityLog;
import com.saltlux.deepsignal.web.repository.UserActivityLogRepository;
import com.saltlux.deepsignal.web.service.IUserActivityLogService;
import com.saltlux.deepsignal.web.service.dto.TrainingDocumentInfoDTO;
import com.saltlux.deepsignal.web.util.HttpRequestResponseUtils;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class UserActivityLogService implements IUserActivityLogService {

    private UserActivityLogRepository userActivityLogRepository;

    public UserActivityLogService(UserActivityLogRepository userActivityLogRepository) {
        this.userActivityLogRepository = userActivityLogRepository;
    }

    @Override
    public UserActivityLog saveUserActivityLog(UserActivityLog userActivityLog) {
        return userActivityLogRepository.save(userActivityLog);
    }

    @Override
    public List<UserActivityLog> saveAllUserActivityLogs(List<UserActivityLog> listUserActivityLog) {
        return userActivityLogRepository.saveAll(listUserActivityLog);
    }

    @Override
    public UserActivityLog saveUserActivityLogWithBase(UserActivityLog userActivityLog) {
        UserActivityLog userActivityLogModel = new UserActivityLog(userActivityLog);
        return userActivityLogRepository.save(userActivityLogModel);
    }

    @Override
    public UserActivityLog saveUserActivityLogWithBase() {
        return userActivityLogRepository.save(makeUserActivityLogBase());
    }

    private UserActivityLog makeUserActivityLogBase() {
        UserActivityLog userActivityLog = null;
        if (null != HttpRequestResponseUtils.getCurrentUser()) {
            userActivityLog = new UserActivityLog();
            userActivityLog.setUsername(HttpRequestResponseUtils.getCurrentUser().getUsername());
            userActivityLog.setUserInfo(HttpRequestResponseUtils.getLoggedInUser());
            userActivityLog.setUserIp(HttpRequestResponseUtils.getClientIpAddress());
            userActivityLog.setRequestMethod(HttpRequestResponseUtils.getRequestMethod());
            userActivityLog.setURL(HttpRequestResponseUtils.getRequestUrl());
            userActivityLog.setPage(HttpRequestResponseUtils.getRequestUri());
            userActivityLog.setQueryParams(HttpRequestResponseUtils.getPageQueryString());
            userActivityLog.setRefererPage(HttpRequestResponseUtils.getRefererPage());
            userActivityLog.setUserAgent(HttpRequestResponseUtils.getUserAgent());
            userActivityLog.setLoggedTime(Instant.now());
        }
        return userActivityLog;
    }

    @Override
    public List<TrainingDocumentInfoDTO> getTrainingDocumentsByKeyword(
        String connectomeId,
        String keyword,
        String dateFrom,
        String dateTo,
        Integer hour
    ) {
        List<UserActivityLog> userActivityLogs = new ArrayList<>();
        JSONObject keywordJSON = new JSONObject();
        keywordJSON.put("keyword", keyword);
        if (Objects.isNull(hour)) {
            userActivityLogs =
                userActivityLogRepository.findUserActivityLogByTrainingKeywordOrType(
                    connectomeId,
                    keywordJSON.toString(),
                    dateFrom,
                    dateTo
                );
        } else {
            userActivityLogs =
                userActivityLogRepository.findUserActivityLogByTrainingKeywordOrType(
                    connectomeId,
                    keywordJSON.toString(),
                    dateFrom,
                    dateTo,
                    hour
                );
        }

        List<TrainingDocumentInfoDTO> trainingDocumentsWithKeyword = new ArrayList<>();
        try {
            for (UserActivityLog log : userActivityLogs) {
                JSONArray logJSON = new JSONArray(log.getTrainingData());
                for (int i = 0; i < logJSON.length(); i++) {
                    JSONObject documentJSON = (JSONObject) logJSON.get(i);
                    if (documentJSON.has("keyword") && documentJSON.getString("keyword").equalsIgnoreCase(keyword)) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        TrainingDocumentInfoDTO trainingDocumentInfoDTO = objectMapper.readValue(
                            documentJSON.toString(),
                            TrainingDocumentInfoDTO.class
                        );
                        trainingDocumentInfoDTO.setDatetime(log.getLoggedTime());
                        trainingDocumentsWithKeyword.add(trainingDocumentInfoDTO);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return trainingDocumentsWithKeyword;
    }

    @Override
    public List<TrainingDocumentInfoDTO> getTrainingDocumentsByType(
        String connectomeId,
        String type,
        String dateFrom,
        String dateTo,
        Integer hour
    ) {
        List<UserActivityLog> userActivityLogs = new ArrayList<>();
        if (type.equalsIgnoreCase(Constants.UploadType.ALL.toString())) {
            if (Objects.isNull(hour)) {
                userActivityLogs = userActivityLogRepository.findUserActivityLogByConnectomeIdAndLoggedTime(connectomeId, dateFrom, dateTo);
            } else {
                userActivityLogs =
                    userActivityLogRepository.findUserActivityLogByConnectomeIdAndLoggedTime(connectomeId, dateFrom, dateTo, hour);
            }
        } else {
            JSONObject typeJSON = new JSONObject();
            typeJSON.put("type", type.toUpperCase());
            if (Objects.isNull(hour)) {
                userActivityLogs =
                    userActivityLogRepository.findUserActivityLogByTrainingKeywordOrType(
                        connectomeId,
                        typeJSON.toString(),
                        dateFrom,
                        dateTo
                    );
            } else {
                userActivityLogs =
                    userActivityLogRepository.findUserActivityLogByTrainingKeywordOrType(
                        connectomeId,
                        typeJSON.toString(),
                        dateFrom,
                        dateTo,
                        hour
                    );
            }
        }

        List<TrainingDocumentInfoDTO> trainingDocumentsWithKeyword = new ArrayList<>();
        try {
            for (UserActivityLog log : userActivityLogs) {
                JSONArray logJSON = new JSONArray(log.getTrainingData());
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                for (int i = 0; i < logJSON.length(); i++) {
                    JSONObject trainingDocJSON = (JSONObject) logJSON.get(i);
                    if (type.equalsIgnoreCase(Constants.UploadType.ALL.toString())) {
                        TrainingDocumentInfoDTO trainingDocumentInfoDTO = objectMapper.readValue(
                            logJSON.get(i).toString(),
                            TrainingDocumentInfoDTO.class
                        );
                        trainingDocumentInfoDTO.setDatetime(log.getLoggedTime());
                        trainingDocumentsWithKeyword.add(trainingDocumentInfoDTO);
                    } else if (trainingDocJSON.has("type") && trainingDocJSON.getString("type").equalsIgnoreCase(type)) {
                        TrainingDocumentInfoDTO trainingDocumentInfoDTO = objectMapper.readValue(
                            logJSON.get(i).toString(),
                            TrainingDocumentInfoDTO.class
                        );
                        trainingDocumentInfoDTO.setDatetime(log.getLoggedTime());
                        trainingDocumentsWithKeyword.add(trainingDocumentInfoDTO);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return trainingDocumentsWithKeyword;
    }
}
