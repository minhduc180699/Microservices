package com.saltlux.deepsignal.web.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryInfo implements Comparable<CountryInfo> {

    private String name;
    private String code;

    @Override
    public int compareTo(CountryInfo o) {
        return this.code.compareTo(o.code);
    }
}
