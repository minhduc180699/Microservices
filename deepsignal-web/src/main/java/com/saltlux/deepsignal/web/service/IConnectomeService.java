package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.service.dto.ConnectomeDTO;
import java.util.List;

public interface IConnectomeService extends GeneralService<Connectome, String> {
    Connectome saveConnectome(ConnectomeDTO connectomeDTO);

    ConnectomeDTO findByConnectomeId(String id);

    List<Connectome> findByUserLogin(String login);
}
