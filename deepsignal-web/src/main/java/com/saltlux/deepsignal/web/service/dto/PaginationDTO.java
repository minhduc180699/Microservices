package com.saltlux.deepsignal.web.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDTO {

    private int page;
    private int size;
    private String orderBy;
    private String sortDirection;

    public PaginationDTO(int page, int size) {
        this.page = page;
        this.size = size;
        this.orderBy = "id";
        this.sortDirection = "desc";
    }
}
