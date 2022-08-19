package com.saltlux.deepsignal.web.service.dto;

import com.saltlux.deepsignal.web.domain.Category;
import java.time.Instant;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.*;

@Data
public class InquiryQuestionDTO {

    private Long id;
    private Category category;

    @NotBlank
    @Size(min = 1, max = 1000)
    private String title;

    @NotBlank
    @Size(min = 1, max = 2000)
    private String content;

    private String file;
    private boolean isPublic;

    private boolean isEmail;
    private UserDTO user;

    @NotBlank
    @Email
    @Size(min = 5, max = 100)
    private String emailName;

    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    private Instant createdDate;

    private InquiryAnswerDTO inquiryAnswer;
}
