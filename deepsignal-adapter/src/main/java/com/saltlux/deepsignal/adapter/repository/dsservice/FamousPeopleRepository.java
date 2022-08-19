package com.saltlux.deepsignal.adapter.repository.dsservice;

import java.util.Optional;

import com.saltlux.deepsignal.adapter.domain.FamousPeople;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamousPeopleRepository extends MongoRepository<FamousPeople, ObjectId> {
    Optional<FamousPeople> findByTitle(String title);
}