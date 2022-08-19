package com.saltlux.deepsignal.web.service.dto;

import com.saltlux.deepsignal.web.domain.User;
import java.time.Instant;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemoDTO {

    private Long id;

    private String content;

    private Instant createdDate = Instant.now();

    @NotNull
    private String feedId;

    private String userId;
}
