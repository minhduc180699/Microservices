package com.ds.dssearcher.service;

import com.ds.dssearcher.entity.EntityLinking;
import com.ds.dssearcher.entity.ResponseEntityLinking;
import com.ds.dssearcher.repository.EntityLinkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntityLinkingServiceImpl implements IEntityLinkingService {


    @Autowired
    private EntityLinkingRepository entityLinkingRepository;

    @Override
    public ResponseEntityLinking entityLinkingSearch(String requestId, String docId, String entityLinkingType) {
        EntityLinking entityLinking = null;
//        if(entityLinkingType.equalsIgnoreCase("full")){
//            entityLinking = entityLinkingRepository.findByDocIdAndResponseEntityLinkingExists(docId,true);
//        }else if(entityLinkingType.equalsIgnoreCase("summary")){
//            entityLinking = entityLinkingRepository.findByDocIdAndResponseEntityLinkingSummaryExists(docId,true);
//        }else if(entityLinkingType.equalsIgnoreCase("all")){
//            entityLinking = entityLinkingRepository.findByDocIdAndResponseEntityLinkingExistsAndResponseEntityLinkingSummaryExists(docId,true,true);
//        }
        entityLinking = entityLinkingRepository.findByDocId(docId);
        if(entityLinking == null){
            return new ResponseEntityLinking("NO_DATA",102,requestId,docId,null,null);
        }
        return  new ResponseEntityLinking("SUCCESS",0,requestId,docId,entityLinkingType.equalsIgnoreCase("summary")? null : entityLinking.getResponseEntityLinking(),
                entityLinkingType.equalsIgnoreCase("full") ?  null : entityLinking.getResponseEntityLinkingSummary());
    }
}
