package com.saltlux.deepsignal.adapter.service;

import com.saltlux.deepsignal.adapter.domain.Connectome;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface IConnectomeService {
    Page<Connectome> findAll(int page, int size, String orderBy, String sortDirection);
    Optional<Connectome> findById(String id);
    Optional<Connectome> findByIdAndLang(String id, String Lang);
    List<Connectome> findByConnectomeId(String connectome_id);
    List<Connectome> findByConnectomeIdAndLang(String connectome_id, String Lang);
}
