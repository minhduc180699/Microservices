package com.saltlux.deepsignal.adapter.secondary.service;

import com.saltlux.deepsignal.adapter.secondary.domain.MarketCodeMaster;
import com.saltlux.deepsignal.adapter.secondary.repository.MarketCodeMasterRepository;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MarketCodeService {

    private final MarketCodeMasterRepository masterRepository;

    public MarketCodeService(MarketCodeMasterRepository masterRepository) {
        this.masterRepository = masterRepository;
    }

    public List<MarketCodeMaster> SearchMarketCode(String search) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "symbolCode"));
        return masterRepository.searchMarketCodeMaster(search, pageable);
    }
}
