package com.saltlux.deepsignal.adapter.repository.dsservice;

import com.saltlux.deepsignal.adapter.domain.People;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends MongoRepository<People, ObjectId> {
    Page<People> findByConnectomeId(Pageable var1, String connectomeId);

    Optional<People> findByConnectomeId(String connectomeId);

    Optional<People> findByConnectomeIdAndLang(String connectomeId, String lang);
}
