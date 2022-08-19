package com.saltlux.deepsignal.adapter.service;

import java.util.Optional;

import com.saltlux.deepsignal.adapter.domain.FamousPeople;

import org.springframework.data.domain.Page;

public interface IFamousPeopleService {
    Page<FamousPeople> findAll(int page, int size, String orderBy, String sortDirection);
    Optional<FamousPeople> findByTitle(String title);
}