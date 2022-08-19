package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.InquiryAnswerEmail;

public interface IInquiryAnswerEmailService extends GeneralService<InquiryAnswerEmail, Long> {
    public InquiryAnswerEmail findByInquiryQuestionId(Long id);
}
