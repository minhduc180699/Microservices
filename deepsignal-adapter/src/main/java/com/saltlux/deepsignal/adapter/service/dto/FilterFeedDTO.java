package com.saltlux.deepsignal.adapter.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterFeedDTO<T> {
    private String field;
    private T value;
}
