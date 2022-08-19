package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.domain.InquiryAnswerEmail;
import com.saltlux.deepsignal.web.repository.InquiryAnswerEmailRepository;
import com.saltlux.deepsignal.web.service.IInquiryAnswerEmailService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InquiryAnswerEmailServiceImpl implements IInquiryAnswerEmailService {

    @Autowired
    private InquiryAnswerEmailRepository inquiryAnswerEmailRepository;

    @Override
    public List<InquiryAnswerEmail> findAll() {
        return null;
    }

    @Override
    public Optional<InquiryAnswerEmail> findById(Long aLong) {
        return inquiryAnswerEmailRepository.findById(aLong);
    }

    @Override
    public InquiryAnswerEmail save(InquiryAnswerEmail inquiryAnswerEmail) {
        return inquiryAnswerEmailRepository.save(inquiryAnswerEmail);
    }

    @Override
    public void remove(Long aLong) {}

    @Override
    public InquiryAnswerEmail findByInquiryQuestionId(Long id) {
        return inquiryAnswerEmailRepository.findByInquiryQuestionId(id);
    }
}
