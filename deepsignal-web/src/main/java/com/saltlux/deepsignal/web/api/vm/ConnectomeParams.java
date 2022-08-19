package com.saltlux.deepsignal.web.api.vm;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectomeParams {

    //    private Integer limitMainClusterSize;

    //    private Integer limitSubClusterSize;

    private String sourceLang;

    private String connectomeId;
    //    private Boolean useUploadData;

    //    private String title;

    //    private List<String> targetLangList;
}
