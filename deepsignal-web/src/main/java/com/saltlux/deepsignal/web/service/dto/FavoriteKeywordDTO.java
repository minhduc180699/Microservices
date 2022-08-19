package com.saltlux.deepsignal.web.service.dto;

import java.time.Instant;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FavoriteKeywordDTO {

    private Long id;

    private String content;

    private String userId;

    private String connectomeId;

    private Instant createdDate = Instant.now();

    private Instant lastModifiedDate = Instant.now();
}
