package com.saltlux.deepsignal.adapter.service.impl;

import java.util.Optional;

import com.saltlux.deepsignal.adapter.domain.FamousCompany;
import com.saltlux.deepsignal.adapter.repository.dsservice.FamousCompanyRepository;
import com.saltlux.deepsignal.adapter.service.IFamousCompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FamousCompanyImpl implements IFamousCompanyService {

    @Autowired
    private FamousCompanyRepository famousCompanyRepository;

    // public FamousCompanyImpl(FamousCompanyRepository famousCompanyRepository) {
    // this.famousCompanyRepository = famousCompanyRepository;
    // }

    @Override
    public Optional<FamousCompany> findByTitle(String title) {
        return famousCompanyRepository.findByTitle(title);
    }

    @Override
    public Page<FamousCompany> findAll(int page, int size, String orderBy, String sortDirection) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
                orderBy);
        return famousCompanyRepository.findAll(pageable);
    }
}
