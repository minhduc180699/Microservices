package com.saltlux.deepsignal.web.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Pagging {

    public static Pageable pageable(int page, int size, String orderBy, String sortDirection) {
        return PageRequest.of(page, size, "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC, orderBy);
    }
}
