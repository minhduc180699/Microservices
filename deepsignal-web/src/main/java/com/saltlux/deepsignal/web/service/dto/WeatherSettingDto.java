package com.saltlux.deepsignal.web.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherSettingDto {

    private Boolean showWeatherCard;
    private Boolean alwaysDetectLocation;
    private String temperatureUnit;
}
