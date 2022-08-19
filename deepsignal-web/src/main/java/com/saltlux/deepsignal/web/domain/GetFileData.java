package com.saltlux.deepsignal.web.domain;

import java.util.List;
import lombok.Data;

@Data
public class GetFileData {

    private List<FileInfo> fileInfoList;
    private String date;
    private int totalPage;
}
