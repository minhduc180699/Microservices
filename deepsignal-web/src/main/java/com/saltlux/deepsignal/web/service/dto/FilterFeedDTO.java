package com.saltlux.deepsignal.web.service.dto;

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

    @Override
    public String toString() {
        return field + ":" + value.toString();
    }
}
