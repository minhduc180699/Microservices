package com.saltlux.deepsignal.adapter.repository.dsservice;

import com.saltlux.deepsignal.adapter.domain.ConnectomeFeed;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectomeFeedRepository extends MongoRepository<ConnectomeFeed, ObjectId> {
    Page<ConnectomeFeed> findByConnectomeId(Pageable var1, String connectomeId);
}
