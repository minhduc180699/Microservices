package com.saltlux.deepsignal.feedcache.service.asyn;

import com.saltlux.deepsignal.feedcache.api.client.SearcherClient;
import com.saltlux.deepsignal.feedcache.model.DocModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.concurrent.Callable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetListDocFromES implements Callable<List<DocModel>> {

    private String connectomeId;
    private String keyword;
    private String from;
    private String until;
    private Integer page;
    private Integer size;
    private String searchType;
    private String channels;
    private String lang;
    private String type;
    private String sortBy;
    private Float score;
    private List<String> writer;

    private SearcherClient searcherClient;

    @Override
    public List<DocModel> call() throws Exception {
        return searcherClient.searchFeed(connectomeId, keyword, from, until, page, size, searchType, channels, lang, type, sortBy, score, writer).getData();
    }
}
