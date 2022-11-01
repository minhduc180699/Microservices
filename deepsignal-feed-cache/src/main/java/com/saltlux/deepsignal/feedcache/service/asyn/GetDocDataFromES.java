package com.saltlux.deepsignal.feedcache.service.asyn;

import com.saltlux.deepsignal.feedcache.api.client.SearcherClient;
import com.saltlux.deepsignal.feedcache.model.DocContentModel;
import com.saltlux.deepsignal.feedcache.model.DocDataModel;
import com.saltlux.deepsignal.feedcache.model.DocModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetDocDataFromES implements Callable<DocDataModel> {

    private String connectomeId;
    private String docId;

    private SearcherClient searcherClient;

    private static final Logger logger = LoggerFactory.getLogger(GetDocDataFromES.class);

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public DocDataModel call() throws Exception {

        GetDocFromES getDocFromES = new GetDocFromES(this.connectomeId, this.docId, this.searcherClient);
        GetDocContentFromES getDocContentFromES = new GetDocContentFromES(this.docId, this.searcherClient);
        Future<DocModel> docModelFuture = executorService.submit(getDocFromES);
        Future<DocContentModel> docContentModelFuture = executorService.submit(getDocContentFromES);
        DocModel docModel = docModelFuture.get(5, TimeUnit.SECONDS);
        DocContentModel docContentModel = docContentModelFuture.get(5, TimeUnit.SECONDS);
        DocDataModel docDataModel = new DocDataModel();
        try {
            docDataModel.buildFeedDataModel(docModel, docContentModel);
        } catch (NullPointerException e) {
            logger.error(String.format("connectomeId: %s, docId: %s, document from ES null , error: %s", connectomeId, docId , e.getMessage()));
            docDataModel.setRequestId(null);
            docDataModel.setCreated_by(null);
            docDataModel.setCollector(null);
        }

        return docDataModel;
    }
}
