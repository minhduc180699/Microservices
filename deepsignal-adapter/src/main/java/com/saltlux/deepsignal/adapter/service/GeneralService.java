package com.saltlux.deepsignal.adapter.service;

import java.util.List;
import java.util.Optional;

public interface GeneralService<T> {
    List<T> findAll();

    Optional<T> findById(Long id);

    T save(T t);

    List<T> saveAll(List<T> var1);

    void remove(Long id);
}
