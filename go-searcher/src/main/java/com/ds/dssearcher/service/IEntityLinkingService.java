package com.ds.dssearcher.service;

import com.ds.dssearcher.entity.ResponseEntityLinking;
import com.ds.dssearcher.entity.ResponseEntityLinkingSummary;

public interface IEntityLinkingService {
    public ResponseEntityLinking entityLinkingSearch( String requestId, String docId,String entityLinkingType);
   // public ResponseEntityLinkingSummary entityLinkingSummanrySearch(String requestId, String docId, String entityLinkingType);

}
