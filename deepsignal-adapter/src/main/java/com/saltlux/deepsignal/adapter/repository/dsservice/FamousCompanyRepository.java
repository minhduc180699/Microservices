package com.saltlux.deepsignal.adapter.repository.dsservice;

import java.util.Optional;

import com.saltlux.deepsignal.adapter.domain.FamousCompany;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamousCompanyRepository extends MongoRepository<FamousCompany, ObjectId> {
    Optional<FamousCompany> findByTitle(String title);
}