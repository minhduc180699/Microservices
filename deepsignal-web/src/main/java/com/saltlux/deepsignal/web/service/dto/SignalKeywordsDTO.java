package com.saltlux.deepsignal.web.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SignalKeywordsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    private String keywords;

    @Size(min = 1, max = 100)
    private String mainKeyword;

    @JsonIgnore
    private String type;

    @NotNull
    private String language;

    @NotNull
    private Integer status;

    private Instant createdDate;
}
