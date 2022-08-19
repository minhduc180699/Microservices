package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.domain.InquiryQuestion;
import com.saltlux.deepsignal.web.exception.ResourceNotFoundException;
import com.saltlux.deepsignal.web.repository.InquiryAnswerRepository;
import com.saltlux.deepsignal.web.repository.InquiryQuestionRepository;
import com.saltlux.deepsignal.web.service.IInquiryQuestionService;
import com.saltlux.deepsignal.web.service.dto.InquiryAnswerDTO;
import com.saltlux.deepsignal.web.service.dto.InquiryQuestionDTO;
import com.saltlux.deepsignal.web.service.dto.PagedResponse;
import com.saltlux.deepsignal.web.util.ObjectMapperUtils;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class IInquiryQuestionServiceImpl extends Pagging implements IInquiryQuestionService {

    @Autowired
    private InquiryQuestionRepository inquiryQuestionRepository;

    @Autowired
    private InquiryAnswerRepository inquiryAnswerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<InquiryQuestion> findAll() {
        return inquiryQuestionRepository.findAll();
    }

    @Override
    public Optional<InquiryQuestion> findById(Long aLong) {
        return inquiryQuestionRepository.findById(aLong);
    }

    @Override
    public InquiryQuestion save(InquiryQuestion inquiryQuestion) {
        return inquiryQuestionRepository.save(inquiryQuestion);
    }

    @Override
    public void remove(Long aLong) {
        inquiryQuestionRepository.deleteById(aLong);
    }

    @Override
    public InquiryQuestionDTO save(InquiryQuestionDTO inquiryQuetionDTO) {
        InquiryQuestion inquiryQuestion = modelMapper.map(inquiryQuetionDTO, InquiryQuestion.class);
        InquiryQuestionDTO dto = ObjectMapperUtils.map(inquiryQuestionRepository.save(inquiryQuestion), InquiryQuestionDTO.class);
        return dto;
    }

    @Override
    public InquiryQuestionDTO findByInquiryQuestionId(Long id) {
        InquiryQuestion inquiryQuestion = inquiryQuestionRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inquiry question", "id", id));
        InquiryQuestionDTO inquiryQuestionDTO = ObjectMapperUtils.map(inquiryQuestion, InquiryQuestionDTO.class);
        InquiryAnswerDTO inquiryAnswerDTO = ObjectMapperUtils.map(
            inquiryAnswerRepository.findByInquiryQuestionId(id),
            InquiryAnswerDTO.class
        );
        inquiryQuestionDTO.setInquiryAnswer(inquiryAnswerDTO);
        return inquiryQuestionDTO;
    }

    @Override
    public PagedResponse<InquiryQuestionDTO> findByUser(int page, int size, String orderBy, String sortDirection, String userId) {
        Page<InquiryQuestion> inquiryQuestionPage = inquiryQuestionRepository.findByUserId(
            pageable(page, size, orderBy, sortDirection),
            userId
        );
        List<InquiryQuestionDTO> faqDTOList = ObjectMapperUtils.mapAll(inquiryQuestionPage.getContent(), InquiryQuestionDTO.class);
        return new PagedResponse<>(
            faqDTOList,
            page,
            size,
            inquiryQuestionPage.getTotalElements(),
            inquiryQuestionPage.getTotalPages(),
            page + 1 >= inquiryQuestionPage.getTotalPages()
        );
    }

    @Override
    public void changeStatus(Long id, Integer status) {
        InquiryQuestion inquiryQuestion = inquiryQuestionRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inquiry question", "id", id));
        inquiryQuestion.setStatus(status);
        inquiryQuestionRepository.save(inquiryQuestion);
    }
}
