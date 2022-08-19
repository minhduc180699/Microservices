package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.LinkShare;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkShareRepository extends JpaRepository<LinkShare, Long> {
    List<LinkShare> findAllByConnectome_ConnectomeIdAndCardId(String connectomeId, String cardId);
}
