package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.InquiryAnswerEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryAnswerEmailRepository extends JpaRepository<InquiryAnswerEmail, Long> {
    public InquiryAnswerEmail findByInquiryQuestionId(Long id);
}
