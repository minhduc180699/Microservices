package com.saltlux.deepsignal.adapter.service;

import com.saltlux.deepsignal.adapter.domain.SignalTodayIssue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISignalTodayIssueService {
    Page<SignalTodayIssue> paging(Pageable pageable, String connectomeId, String workDay, String signalType);

    void deleteById(String id);
}
