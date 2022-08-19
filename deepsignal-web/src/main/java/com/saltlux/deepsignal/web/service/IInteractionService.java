package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.Interaction;
import java.util.List;
import java.util.Optional;

public interface IInteractionService {
    Interaction saveByType(int type);

    Optional<Interaction> findByType(int type);

    Interaction saveIfNoneExist(int type);

    List<Interaction> findAll();
}
