package com.saltlux.deepsignal.adapter.repository.dsservice;

import com.saltlux.deepsignal.adapter.domain.Connectome;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectomeRepository extends MongoRepository<Connectome, String> {
    List<Connectome> findByConnectomeId(String connectome_id);
    List<Connectome> findByConnectomeIdAndLang(String connectome_id, String lang);
}
