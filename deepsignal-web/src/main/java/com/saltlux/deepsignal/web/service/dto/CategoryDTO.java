package com.saltlux.deepsignal.web.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDTO {

    @NotBlank
    @Size(min = 1, max = 50)
    private String code;

    private Integer type;
}
