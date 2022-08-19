package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.InquiryQuestion;
import com.saltlux.deepsignal.web.service.dto.InquiryQuestionDTO;
import com.saltlux.deepsignal.web.service.dto.PagedResponse;

public interface IInquiryQuestionService extends GeneralService<InquiryQuestion, Long> {
    InquiryQuestionDTO save(InquiryQuestionDTO inquiryQuestionDTO);

    InquiryQuestionDTO findByInquiryQuestionId(Long id);

    PagedResponse<InquiryQuestionDTO> findByUser(int page, int size, String orderBy, String sortDirection, String userId);

    void changeStatus(Long id, Integer status);
}
