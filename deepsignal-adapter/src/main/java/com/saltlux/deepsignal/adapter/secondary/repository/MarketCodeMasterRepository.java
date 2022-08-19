package com.saltlux.deepsignal.adapter.secondary.repository;

import com.saltlux.deepsignal.adapter.secondary.domain.MarketCodeMaster;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketCodeMasterRepository extends JpaRepository<MarketCodeMaster, Long> {
    @Query(
        "SELECT m FROM MarketCodeMaster m WHERE lower(m.symbolCode) like lower(concat('%', :search, '%'))" +
        "OR lower(m.symbolNameEn) like lower(concat('%', :search, '%'))" +
        "OR lower(m.symbolNameKr) like lower(concat('%', :search, '%'))"
    )
    List<MarketCodeMaster> searchMarketCodeMaster(@Param("search") String search, Pageable pageable);
}
