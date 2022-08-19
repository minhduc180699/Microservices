package com.saltlux.deepsignal.adapter.service.impl;

import com.saltlux.deepsignal.adapter.domain.dstornado.DsRealtimeMetaSearch;
import com.saltlux.deepsignal.adapter.repository.dstornado.DsRealtimeMetasearchRepository;
import com.saltlux.deepsignal.adapter.service.IDsRealtimeMetasearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DsRealtimeMetasearchServiceImpl implements IDsRealtimeMetasearchService {

    @Autowired
    private DsRealtimeMetasearchRepository dsRealtimeMetasearchRepository;

    @Override
    public Page<DsRealtimeMetaSearch> findByKeyword(int page, int size, String orderBy, String sortDirection, String keyword) {
        Pageable pageable = PageRequest.of(
            page,
            size,
            "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
            orderBy
        );
        Page<DsRealtimeMetaSearch> dsRealtimeMetaSearches = dsRealtimeMetasearchRepository.findByKeywordLike(pageable, keyword);
        return dsRealtimeMetaSearches;
    }
}
