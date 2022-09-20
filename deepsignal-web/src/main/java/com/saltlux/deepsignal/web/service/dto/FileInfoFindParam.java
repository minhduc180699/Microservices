package com.saltlux.deepsignal.web.service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoFindParam {

    private String path;
    private String userId;
    private String type;
    private Integer chromeType;
}
