package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.domain.Faqs;
import com.saltlux.deepsignal.web.repository.FaqsRepository;
import com.saltlux.deepsignal.web.service.IFaqService;
import com.saltlux.deepsignal.web.service.dto.FaqDTO;
import com.saltlux.deepsignal.web.service.dto.PagedResponse;
import com.saltlux.deepsignal.web.util.ObjectMapperUtils;
import java.util.List;
import java.util.Optional;
import org.hibernate.validator.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FaqServiceImpl extends Pagging implements IFaqService {

    @Autowired
    private FaqsRepository faqsRepository;

    @Override
    public List<Faqs> findAll() {
        return faqsRepository.findAll();
    }

    @Override
    public Optional<Faqs> findById(Long aLong) {
        return faqsRepository.findById(aLong);
    }

    @Override
    public Faqs save(Faqs faqs) {
        return faqsRepository.save(faqs);
    }

    @Override
    public void remove(Long aLong) {
        faqsRepository.deleteById(aLong);
    }

    @Override
    public PagedResponse<FaqDTO> findByCategoryId(String categoryCode, int page, int size, String orderBy, String sortDirection) {
        Page<Faqs> faqsPage = null;
        if (StringHelper.isNullOrEmptyString(categoryCode)) {
            return findAll(page, size, orderBy, sortDirection);
        } else {
            faqsPage = faqsRepository.findByCategoryCode(pageable(page, size, orderBy, sortDirection), categoryCode);
        }
        List<FaqDTO> faqDTOList = ObjectMapperUtils.mapAll(faqsPage.getContent(), FaqDTO.class);
        return new PagedResponse<>(
            faqDTOList,
            page,
            size,
            faqsPage.getTotalElements(),
            faqsPage.getTotalPages(),
            page + 1 >= faqsPage.getTotalPages()
        );
    }

    @Override
    public PagedResponse<FaqDTO> searchKeyWord(int page, int size, String orderBy, String sortDirection, String keySearch, String code) {
        Page<Faqs> faqsPage = faqsRepository.findByTitleContainingOrQuestionContainingOrAnswerContainingAndCategoryCodeEquals(
            code,
            keySearch,
            pageable(page, size, orderBy, sortDirection)
        );
        List<FaqDTO> faqDTOList = ObjectMapperUtils.mapAll(faqsPage.getContent(), FaqDTO.class);
        return new PagedResponse<>(
            faqDTOList,
            page,
            size,
            faqsPage.getTotalElements(),
            faqsPage.getTotalPages(),
            page + 1 >= faqsPage.getTotalPages()
        );
    }

    @Override
    public PagedResponse<FaqDTO> findAll(int page, int size, String orderBy, String sortDirection) {
        Page<Faqs> faqsPage = faqsRepository.findAll(pageable(page, size, orderBy, sortDirection));
        List<FaqDTO> faqDTOList = ObjectMapperUtils.mapAll(faqsPage.getContent(), FaqDTO.class);
        return new PagedResponse<>(
            faqDTOList,
            page,
            size,
            faqsPage.getTotalElements(),
            faqsPage.getTotalPages(),
            page + 1 >= faqsPage.getTotalPages()
        );
    }
}
