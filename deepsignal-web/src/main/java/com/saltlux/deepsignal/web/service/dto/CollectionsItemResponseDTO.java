package com.saltlux.deepsignal.web.service.dto;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectionsItemResponseDTO {

    private String collectionId;
    private List<String> documentIdList;
    private Date modifiedDate;
}
