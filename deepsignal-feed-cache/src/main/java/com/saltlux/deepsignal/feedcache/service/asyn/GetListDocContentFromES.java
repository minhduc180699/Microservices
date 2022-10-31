package com.saltlux.deepsignal.feedcache.service.asyn;

import com.saltlux.deepsignal.feedcache.api.client.SearcherClient;
import com.saltlux.deepsignal.feedcache.model.DocContentModel;
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
public class GetListDocContentFromES implements Callable<List<DocContentModel>> {

    private SearcherClient searcherClient;

    @Override
    public List<DocContentModel> call() throws Exception {
        return null;
    }
}
