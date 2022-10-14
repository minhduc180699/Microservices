package com.saltlux.deepsignal.web.service.dto;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectionResponseDTO {

    private String connectomeId;
    private String collectionId;
    private String lang;
    private List<ConnectomeNodeDTO> connectomeNodeList;
    private List<String> documentIdList;
    private List<String> keywordList;
    private Date modifiedDate;
    private collectionStatus status;
}

@Getter
@Setter
class collectionStatus {

    private int code;
    private String message;
}
