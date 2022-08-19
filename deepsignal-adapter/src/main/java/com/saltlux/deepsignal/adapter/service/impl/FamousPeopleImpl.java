package com.saltlux.deepsignal.adapter.service.impl;

import java.util.Optional;

import com.saltlux.deepsignal.adapter.domain.FamousPeople;
import com.saltlux.deepsignal.adapter.repository.dsservice.FamousPeopleRepository;
import com.saltlux.deepsignal.adapter.service.IFamousPeopleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FamousPeopleImpl implements IFamousPeopleService {

    @Autowired
    private FamousPeopleRepository famousPeopleRepository;

    // public FamousCompanyImpl(FamousCompanyRepository famousCompanyRepository) {
    // this.famousCompanyRepository = famousCompanyRepository;
    // }

    @Override
    public Optional<FamousPeople> findByTitle(String title) {
        return famousPeopleRepository.findByTitle(title);
    }

    @Override
    public Page<FamousPeople> findAll(int page, int size, String orderBy, String sortDirection) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
                orderBy);
        return famousPeopleRepository.findAll(pageable);
    }
}
