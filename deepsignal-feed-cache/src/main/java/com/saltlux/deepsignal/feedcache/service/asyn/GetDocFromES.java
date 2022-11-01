package com.saltlux.deepsignal.feedcache.service.asyn;

import com.saltlux.deepsignal.feedcache.api.client.SearcherClient;
import com.saltlux.deepsignal.feedcache.model.DocModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetDocFromES implements Callable<DocModel> {

    private String connectomeId;
    private String docId;

    private SearcherClient searcherClient;

    @Override
    public DocModel call() throws Exception {
        return searcherClient.getFeed(connectomeId,docId).getData();
    }
}
