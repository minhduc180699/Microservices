package com.ds.dssearcher.repository;

import com.ds.dssearcher.entity.EntityLinking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityLinkingRepository extends MongoRepository<EntityLinking, String> {

    public EntityLinking findByDocIdAndResponseEntityLinkingExists(String docId,Boolean check);
    public EntityLinking findByDocIdAndResponseEntityLinkingSummaryExists(String docId,Boolean check);
    public EntityLinking findByDocId(String docId);
    public EntityLinking findByDocIdAndResponseEntityLinkingExistsAndResponseEntityLinkingSummaryExists(String docId,Boolean check,Boolean checkSummary);
}
