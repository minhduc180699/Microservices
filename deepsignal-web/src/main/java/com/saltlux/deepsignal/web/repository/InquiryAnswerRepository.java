package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.InquiryAnswer;
import com.saltlux.deepsignal.web.domain.InquiryQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryAnswerRepository extends JpaRepository<InquiryAnswer, Long> {
    Page<InquiryQuestion> findByUser_Id(Pageable pageable, String userId);
    InquiryAnswer findByInquiryQuestionId(Long id);
}
