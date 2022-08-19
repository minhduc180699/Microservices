package com.saltlux.deepsignal.adapter.service.impl;

import com.saltlux.deepsignal.adapter.config.Constants;
import com.saltlux.deepsignal.adapter.domain.PersonalDocument;
import com.saltlux.deepsignal.adapter.domain.SignalTodayIssue;
import com.saltlux.deepsignal.adapter.repository.dsservice.SignalTodayIssueRepository;
import com.saltlux.deepsignal.adapter.service.ISignalTodayIssueService;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class SignalTodayIssueServiceImpl implements ISignalTodayIssueService {

    private final SignalTodayIssueRepository signalTodayIssueRepository;
    private final MongoTemplate mongoTemplate;

    public SignalTodayIssueServiceImpl(SignalTodayIssueRepository signalTodayIssueRepository, MongoTemplate mongoTemplate) {
        this.signalTodayIssueRepository = signalTodayIssueRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<SignalTodayIssue> paging(Pageable pageable, String connectomeId, String workDay, String signalType) {
        return signalTodayIssueRepository.findAllByConnectomeIdAndWorkDayAndSignalTypeAndIsDeleteNot(connectomeId, workDay, signalType, Constants.PERSONAL_DOCUMENT_STATUS.DELETED, pageable);
    }

    @Override
    public void deleteById(String id) {
        Query query = new Query();
        ObjectId objectId = new ObjectId(id);
        query.addCriteria(Criteria.where("_id").is(objectId));
        Update update = new Update();
        update.set("isDelete", Constants.PERSONAL_DOCUMENT_STATUS.DELETED);
        mongoTemplate.updateMulti(query, update, SignalTodayIssue.class);
    }
}
