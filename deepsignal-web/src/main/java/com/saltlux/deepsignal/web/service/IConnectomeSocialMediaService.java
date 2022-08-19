package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.ConnectomeSocialMedia;
import com.saltlux.deepsignal.web.service.dto.ConnectomeSocialMediaDTO;

public interface IConnectomeSocialMediaService extends GeneralService<ConnectomeSocialMedia, Long> {
    ConnectomeSocialMedia createConnectomeSocialMedial(ConnectomeSocialMediaDTO socialMediaDTO);
}
