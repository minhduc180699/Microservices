package com.saltlux.deepsignal.web.service.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InquiryAnswerDTO {

    private Long id;
    private InquiryQuestionDTO inquiryQuestion;

    @NotBlank
    private String content;

    private String file;

    private UserDTO user;
}
