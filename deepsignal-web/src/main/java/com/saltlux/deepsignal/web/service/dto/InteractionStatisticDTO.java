package com.saltlux.deepsignal.web.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InteractionStatisticDTO {

    private Long totalLike;
    private Long totalDislike;
    private Long totalComment;
    private Long totalShare;
    private Long totalBookmark;
}
