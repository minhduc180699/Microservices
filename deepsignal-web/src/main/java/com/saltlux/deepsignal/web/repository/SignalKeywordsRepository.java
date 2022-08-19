package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.SignalKeywords;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignalKeywordsRepository extends JpaRepository<SignalKeywords, Long> {
    List<SignalKeywords> findSignalKeywordsByConnectome_ConnectomeIdAndStatus(String connectomeId, Integer status);
}
