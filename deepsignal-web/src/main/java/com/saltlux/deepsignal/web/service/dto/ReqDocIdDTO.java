package com.saltlux.deepsignal.web.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReqDocIdDTO {

    private String id;
    private String name;
    private String path;
    private String description;
    private String type;
    private String connectomeId;
    private String author;
    private String searchType;
    private String favicon;
    private String lang;
    private String keyword;
    private String originDate;
    private String img;
}
