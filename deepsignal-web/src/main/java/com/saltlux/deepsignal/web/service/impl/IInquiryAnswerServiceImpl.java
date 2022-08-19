package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.domain.InquiryAnswer;
import com.saltlux.deepsignal.web.domain.InquiryAnswerEmail;
import com.saltlux.deepsignal.web.domain.InquiryQuestion;
import com.saltlux.deepsignal.web.exception.ResourceNotFoundException;
import com.saltlux.deepsignal.web.repository.InquiryAnswerEmailRepository;
import com.saltlux.deepsignal.web.repository.InquiryAnswerRepository;
import com.saltlux.deepsignal.web.repository.InquiryQuestionRepository;
import com.saltlux.deepsignal.web.service.IInquiryAnswerService;
import com.saltlux.deepsignal.web.service.dto.InquiryAnswerDTO;
import com.saltlux.deepsignal.web.util.ObjectMapperUtils;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IInquiryAnswerServiceImpl implements IInquiryAnswerService {

    @Autowired
    private InquiryAnswerRepository inquiryAnswerRepository;

    @Autowired
    private InquiryQuestionRepository inquiryQuestionRepository;

    @Autowired
    private InquiryAnswerEmailRepository inquiryAnswerEmailRepository;

    @Override
    public List<InquiryAnswer> findAll() {
        return null;
    }

    @Override
    public Optional<InquiryAnswer> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    @Transactional
    public InquiryAnswer save(InquiryAnswer inquiryAnswer) {
        return inquiryAnswerRepository.save(inquiryAnswer);
    }

    @Override
    @Transactional
    public void remove(Long aLong) {}

    @Override
    @Transactional
    public InquiryAnswer save(InquiryAnswerDTO inquiryAnswerDTO) {
        InquiryQuestion inquiryQuestion = inquiryQuestionRepository
            .findById(inquiryAnswerDTO.getInquiryQuestion().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Inquiry question", "id", inquiryAnswerDTO.getInquiryQuestion().getId()));

        InquiryAnswer output = inquiryAnswerRepository.save(ObjectMapperUtils.map(inquiryAnswerDTO, InquiryAnswer.class));

        inquiryQuestion.setStatus(1);
        inquiryQuestion = inquiryQuestionRepository.save(inquiryQuestion);

        if (inquiryQuestion.isEmail()) {
            InquiryAnswerEmail inquiryAnswerEmail = new InquiryAnswerEmail();
            inquiryAnswerEmail.setInquiryQuestion(inquiryQuestion);
            inquiryAnswerEmail.setEmail(inquiryQuestion.getEmailName());
            inquiryAnswerEmail.setName(inquiryQuestion.getName());
            inquiryAnswerEmail.setTitle(inquiryQuestion.getTitle());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(output.getContent());
            stringBuilder.append("<br/>");
            stringBuilder.append("<br/>");
            stringBuilder.append(("Question: "));
            stringBuilder.append("<br/>");
            stringBuilder.append(inquiryQuestion.getContent());
            inquiryAnswerEmail.setContent(stringBuilder.toString());
            inquiryAnswerEmail.setFileAnswer(output.getFile());
            inquiryAnswerEmail.setFileQuestion(inquiryQuestion.getFile());
            inquiryAnswerEmailRepository.save(inquiryAnswerEmail);
        }
        return output;
    }

    @Override
    public InquiryAnswer findByInquiryQuestionId(Long id) {
        return inquiryAnswerRepository.findByInquiryQuestionId(id);
    }
}
