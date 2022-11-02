package com.ds.dssearcher.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
public class SearchDTO {

    @NotEmpty(message = "Keyword must be valuable")
    private String keyword;
    private int page =1;
    private String lang;
    private String searchType;
    private String searchDate;
    private int size = 10;

}
