package com.saltlux.deepsignal.web.service.dto;

import java.time.Instant;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class FaqDTO {

    private Long faqId;
    private CategoryDTO category;

    @NotBlank
    @Size(min = 1, max = 1000)
    private String title;

    @NotBlank
    private String question;

    @NotBlank
    private String answer;

    private String files;
    private int viewCount;
    private String note;
    private Instant createDate;
    private UserDTO user;
}
