package com.saltlux.deepsignal.adapter.service;

import org.springframework.data.domain.Page;

public interface IDsRealtimeMetasearchService<T> {
    Page<T> findByKeyword(int page, int size, String orderBy, String sortDirection, String keyword);
}
