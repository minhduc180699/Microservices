package com.saltlux.deepsignal.web.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonRes {

    private int currentPage;
    private int totalPages;
    private long totalItems;
}
