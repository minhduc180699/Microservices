package com.saltlux.deepsignal.web.service;

import java.util.List;
import java.util.Optional;

public interface GeneralService<T, ID> {
    List<T> findAll();

    Optional<T> findById(ID id);

    T save(T t);

    void remove(ID id);
}
