package com.saltlux.deepsignal.web.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saltlux.deepsignal.web.api.errors.BadRequestException;
import com.saltlux.deepsignal.web.api.websocket.dto.MessageWrapper;
import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.repository.UserRepository;
import com.saltlux.deepsignal.web.security.SecurityUtils;
import com.saltlux.deepsignal.web.service.IConnectomeFeedService;
import com.saltlux.deepsignal.web.service.INotificationService;
import com.saltlux.deepsignal.web.service.dto.*;
import com.saltlux.deepsignal.web.util.AppUtil;
import com.saltlux.deepsignal.web.util.ConnectAdapterApi;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RMapCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ConnectomeFeedService implements IConnectomeFeedService {

    private final long TIME_INTERVAL_LOAD_FEED = 10000;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ConnectAdapterApi connectAdapterApi;

    @Autowired
    private INotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ConnectomeFeedDTO getFeedConnectomeByDocId(String docId) throws JsonProcessingException {
        Map<String, Object> uriParam = new HashMap<>();
        uriParam.put("docId", docId);
        String uri = applicationProperties.getExternalApi().getDeepsignalOutside() + "/document/doc/{docId}";
        String strJson = connectAdapterApi.getDataFromAdapterApi(uri, null, HttpMethod.GET, uriParam);
        ObjectMapper objectMapper = new ObjectMapper();
        ConnectomeFeedDTO connectomeFeedDTO = objectMapper.readValue(strJson, ConnectomeFeedDTO.class);
        List<ImageDTO> imageDTOS = new ArrayList<>();
        if (Objects.nonNull(connectomeFeedDTO.getImageLinks())) {
            int sizeImage = connectomeFeedDTO.getImageLinks().size();
            final int fixImage = 5;
            int sizeSubList = Math.min(sizeImage, fixImage);
            for (String source : connectomeFeedDTO.getImageLinks().subList(0, sizeSubList)) {
                ImageDTO imageDTO = AppUtil.getImageSizeByUrl(source);
                if (imageDTO != null) {
                    imageDTOS.add(imageDTO);
                }
            }
            if (sizeImage > fixImage) {
                for (String source : connectomeFeedDTO.getImageLinks().subList(fixImage, sizeImage)) {
                    imageDTOS.add(new ImageDTO(source));
                }
            }
            connectomeFeedDTO.setImageDTOS(imageDTOS);
        }
        Constants.PostType postType = Constants.PostType.BLOG;
        if (connectomeFeedDTO.getServiceType().equalsIgnoreCase(postType.name())) {
            connectomeFeedDTO.setPostType(postType.postType);
        }
        return connectomeFeedDTO;
    }

    @Override
    public long countFeedByConnectome(String connectomeId) {
        String uri = connectAdapterApi.getExternalApi() + "/connectome-feed/countByConnectomeId/{connectomeId}";
        try {
            Map<String, Object> uriParams = new HashMap<>();
            uriParams.put("connectomeId", connectomeId);
            String strJson = connectAdapterApi.getDataFromAdapterApi(uri, null, HttpMethod.GET, uriParams);
            JSONObject jsonObject = new JSONObject(strJson);
            return Long.parseLong(jsonObject.get("totalItems").toString());
        } catch (Exception e) {
            log.error("Parse JSON error from " + uri);
            return 0;
        }
    }
}
