package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.FavoriteKeyword;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteKeywordRepository extends JpaRepository<FavoriteKeyword, Long> {
    boolean existsByContentAndUser_Id(String content, String userId);

    FavoriteKeyword findByContentAndUser_Id(String content, String userId);

    List<FavoriteKeyword> findAllByUser_Id(String userId);

    List<FavoriteKeyword> findAllByUser_Id(String userId, Sort sort);

    void deleteByUser_Id(String userId);
}
