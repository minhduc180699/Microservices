package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCollectionRequestBody {

    private String collectionId;
    private String connectomeId;
    private List<ConnectomeNodeDTO> connectomeNodeList;
    private List<String> documentIdList;
    private String lang;
}
