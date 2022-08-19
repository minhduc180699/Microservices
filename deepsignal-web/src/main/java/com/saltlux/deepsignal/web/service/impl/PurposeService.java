package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.domain.Purpose;
import com.saltlux.deepsignal.web.repository.PurposeRepository;
import com.saltlux.deepsignal.web.service.IPurposeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PurposeService implements IPurposeService {

    @Autowired
    private PurposeRepository purposeRepository;

    @Override
    public List<Purpose> getAll() {
        return purposeRepository.findAll(Sort.by(Sort.Direction.ASC, "purposeName"));
    }
}
