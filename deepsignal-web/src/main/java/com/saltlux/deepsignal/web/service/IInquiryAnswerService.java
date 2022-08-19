package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.InquiryAnswer;
import com.saltlux.deepsignal.web.service.dto.InquiryAnswerDTO;

public interface IInquiryAnswerService extends GeneralService<InquiryAnswer, Long> {
    public InquiryAnswer save(InquiryAnswerDTO inquiryAnswerDTO);

    public InquiryAnswer findByInquiryQuestionId(Long id);
}
