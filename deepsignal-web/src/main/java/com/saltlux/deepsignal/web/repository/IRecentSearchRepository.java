package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.RecentSearch;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IRecentSearchRepository extends PagingAndSortingRepository<RecentSearch, Long> {
    Optional<RecentSearch> findByContentAndUser_Id(String content, String userId);

    Page<RecentSearch> findAllByContentContainingAndUser_Id(Pageable pageable, String content, String userId);

    List<RecentSearch> findAllByUser_Id(Sort sort, String userId);

    @Modifying
    @Query(value = "delete from RecentSearch r where r.user.id = :userId")
    void deleteAllByUserId(@Param("userId") String userId);
}
