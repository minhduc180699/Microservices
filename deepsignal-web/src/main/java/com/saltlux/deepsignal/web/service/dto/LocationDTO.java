package com.saltlux.deepsignal.web.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {

    private float lon;
    private float lat;
    private String country;
    private String city;
    private String regionName;
    private String countryCode;

    public LocationDTO(float lon, float lat) {
        this.lon = lon;
        this.lat = lat;
    }
}
