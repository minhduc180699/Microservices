package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.domain.ChromeBookmarkTrained;
import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.repository.ChromeBookmarkTrainedRepository;
import com.saltlux.deepsignal.web.repository.ConnectomeRepository;
import com.saltlux.deepsignal.web.service.IChromeBookmarkTrainedService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ChromeBookmarkTrainedServiceImpl implements IChromeBookmarkTrainedService {

    private final ChromeBookmarkTrainedRepository chromeBookmarkTrainedRepository;
    private final ConnectomeRepository connectomeRepository;

    public ChromeBookmarkTrainedServiceImpl(
        ChromeBookmarkTrainedRepository chromeBookmarkTrainedRepository,
        ConnectomeRepository connectomeRepository
    ) {
        this.chromeBookmarkTrainedRepository = chromeBookmarkTrainedRepository;
        this.connectomeRepository = connectomeRepository;
    }

    @Override
    public List<ChromeBookmarkTrained> findAllByConnectomeId(String connectomeId) {
        Optional<Connectome> connectome = connectomeRepository.findConnectomeByConnectomeId(connectomeId);
        if (!connectome.isPresent()) {
            return null;
        }
        User user = connectome.get().getUser();
        return chromeBookmarkTrainedRepository.findAllByUserId(user.getId());
    }

    @Override
    public List<ChromeBookmarkTrained> save(List<ChromeBookmarkTrained> lstBookmarkTrained, String connectomeId) throws Exception {
        Optional<Connectome> connectome = connectomeRepository.findConnectomeByConnectomeId(connectomeId);
        if (!connectome.isPresent()) {
            return null;
        }
        User user = connectome.get().getUser();
        lstBookmarkTrained
            .parallelStream()
            .forEach(
                item -> {
                    ChromeBookmarkTrained chromeBookmarkTrained = chromeBookmarkTrainedRepository.findByPathAndUserId(
                        item.getPath(),
                        user.getId()
                    );
                    if (chromeBookmarkTrained != null) item.setId(chromeBookmarkTrained.getId());
                    item.setUserId(user.getId());
                }
            );
        return chromeBookmarkTrainedRepository.saveAll(lstBookmarkTrained);
    }
}
