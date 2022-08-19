package com.saltlux.deepsignal.adapter.repository.dsservice;

import com.saltlux.deepsignal.adapter.domain.SignalTodayIssue;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignalTodayIssueRepository extends MongoRepository<SignalTodayIssue, ObjectId> {
    Page<SignalTodayIssue> findAllByConnectomeIdAndWorkDayAndSignalTypeAndIsDeleteNot(
        String connectomeId,
        String workDay,
        String signalType,
        int isDelete,
        Pageable pageable
    );
}
