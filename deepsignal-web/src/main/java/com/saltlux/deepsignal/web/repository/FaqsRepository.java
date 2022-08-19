package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.Faqs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqsRepository extends JpaRepository<Faqs, Long> {
    Page<Faqs> findByCategoryCode(Pageable var1, String code);

    @Query(
        nativeQuery = true,
        value = "select * from faqs fa where (:code = '' or :code is null or fa.category_code = :code) and (:keySearch = '' or :keySearch is null or fa.title like CONCAT('%',:keySearch,'%') or fa.question like CONCAT('%',:keySearch,'%') or fa.answer like CONCAT('%',:keySearch,'%') )"
    )
    Page<Faqs> findByTitleContainingOrQuestionContainingOrAnswerContainingAndCategoryCodeEquals(
        @Param("code") String code,
        @Param("keySearch") String keySearch,
        Pageable var1
    );

    Page<Faqs> findAll(Pageable var1);
}
