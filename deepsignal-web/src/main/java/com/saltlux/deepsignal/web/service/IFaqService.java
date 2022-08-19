package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.Faqs;
import com.saltlux.deepsignal.web.service.dto.FaqDTO;
import com.saltlux.deepsignal.web.service.dto.PagedResponse;

public interface IFaqService extends GeneralService<Faqs, Long> {
    public PagedResponse<FaqDTO> findByCategoryId(String categoryId, int page, int size, String orderBy, String sortDirection);

    public PagedResponse<FaqDTO> searchKeyWord(int page, int size, String orderBy, String sortDirection, String keySearch, String code);

    public PagedResponse<FaqDTO> findAll(int page, int size, String orderBy, String sortDirection);
}
