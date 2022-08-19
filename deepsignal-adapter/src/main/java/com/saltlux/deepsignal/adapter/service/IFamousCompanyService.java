package com.saltlux.deepsignal.adapter.service;

import java.util.Optional;

import com.saltlux.deepsignal.adapter.domain.FamousCompany;

import org.springframework.data.domain.Page;

public interface IFamousCompanyService {
    Page<FamousCompany> findAll(int page, int size, String orderBy, String sortDirection);
    Optional<FamousCompany> findByTitle(String title);
}