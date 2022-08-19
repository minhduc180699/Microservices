package com.saltlux.deepsignal.web.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherInfoDTO extends LocationDTO {

    @JsonProperty("current")
    private WeatherTimeInfo current;

    @JsonProperty("daily")
    private String daily;

    public WeatherInfoDTO(WeatherInfoDTO weatherInfoDTO, LocationDTO locationDTO) {
        this.current = weatherInfoDTO.getCurrent();
        this.daily = weatherInfoDTO.getDaily();
        this.setCountry(locationDTO.getCountry());
        this.setCity(locationDTO.getRegionName());
        this.setLon(locationDTO.getLon());
        this.setLat(locationDTO.getLat());
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class WeatherTimeInfo {

        private float temp;
        private List<Weather> weather;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Weather {

        private Integer id;
        private String main;
        private String description;
        private String icon;
    }
}
