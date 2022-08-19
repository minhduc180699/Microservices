package com.saltlux.deepsignal.adapter.service.impl;

import com.saltlux.deepsignal.adapter.domain.Connectome;
import com.saltlux.deepsignal.adapter.repository.dsservice.ConnectomeRepository;
import com.saltlux.deepsignal.adapter.service.IConnectomeService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ConnectomeServiceImpl implements IConnectomeService {

    @Autowired
    private ConnectomeRepository connectomeRepository;

    @Override
    public Page<Connectome> findAll(int page, int size, String orderBy, String sortDirection) {
        Pageable pageable = PageRequest.of(
            page,
            size,
            "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
            orderBy
        );
        return connectomeRepository.findAll(pageable);
    }

    @Override
    public Optional<Connectome> findById(String id) {
        return connectomeRepository.findById(id);
    }

    @Override
    public Optional<Connectome> findByIdAndLang(String id, String Lang) {
        Optional<Connectome> res = connectomeRepository.findById(id).filter(elem -> elem.getLang().equals(Lang));
        return res;
    }

    @Override
    public List<Connectome> findByConnectomeId(String connectome_id) {
        List<Connectome> res = connectomeRepository.findByConnectomeId(connectome_id);
        return res;
    }

    @Override
    public List<Connectome> findByConnectomeIdAndLang(String connectome_id, String lang) {
        List<Connectome> res = connectomeRepository.findByConnectomeIdAndLang(connectome_id, lang);
        return res;
    }
}
