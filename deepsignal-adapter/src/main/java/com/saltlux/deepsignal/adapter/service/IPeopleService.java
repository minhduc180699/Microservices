package com.saltlux.deepsignal.adapter.service;

import com.saltlux.deepsignal.adapter.domain.People;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPeopleService {
    Page<People> findAll(int page, int size, String orderBy, String sortDirection);

    Page<People> findByConnectomId(Pageable pageable, String connectomeId);

    Optional<People> findOneByConnectomeId(String connectomeId);

    People getByConnectomeIdAndLang(String connectomeId, String lang);

    Optional<People> findById(String id);

    boolean hiddenPeople(String connectomeId, String title, String type, String lang, Boolean deleted);
}
