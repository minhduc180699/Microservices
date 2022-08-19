package com.saltlux.deepsignal.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saltlux.deepsignal.web.service.dto.LocationDTO;
import com.saltlux.deepsignal.web.service.dto.WeatherInfoDTO;
import com.saltlux.deepsignal.web.util.ConnectAdapterApi;
import io.netty.util.internal.StringUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private final String OPEN_API_LOCATION = "http://ip-api.com/json/{ipAddress}";
    private final String OPEN_API_GEO_BASE = "https://api.openweathermap.org/data/2.5";
    private final String OPEN_API_GEO_ONECALL = OPEN_API_GEO_BASE + "/onecall";
    private final String OPEN_API_GEO_FORECAST = OPEN_API_GEO_BASE + "/forecast";

    // App Id created from Open Weather Website. Please create from this page: https://home.openweathermap.org/api_keys
    // Docs: https://openweathermap.org/guide
    private final String APP_ID_OPEN_WEATHER = "e9a470db9b04d8920507eba870baa8e5";
    // pass EXCLUDE param to remove property from response
    private final String[] EXCLUDES = new String[] { "current", "minutely", "hourly", "daily" };
    private final String[] UNITS = new String[] { "imperial", "metric" };

    @Autowired
    private ConnectAdapterApi connectAdapterApi;

    public LocationDTO getLocationByIp(String ipAddress) throws JsonProcessingException {
        if (!StringUtil.isNullOrEmpty(ipAddress)) {
            Map<String, Object> uriParams = new HashMap<>();
            uriParams.put("ipAddress", ipAddress);
            String strJson = connectAdapterApi.getDataFromAdapterApi(OPEN_API_LOCATION, null, HttpMethod.GET, uriParams);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(strJson, LocationDTO.class);
        }
        return null;
    }

    public WeatherInfoDTO getWeatherInfoByLocation(LocationDTO locationDTO, String language) throws JsonProcessingException {
        Map<String, Object> params = appendBaseParamWeather(locationDTO.getLat(), locationDTO.getLon(), language);
        params.put("exclude", EXCLUDES[1] + "," + EXCLUDES[2] + "," + EXCLUDES[3]);
        params.put("units", UNITS[1]);
        if (Objects.nonNull(language)) {
            params.put("lang", language);
        }
        String strJson = connectAdapterApi.getDataFromAdapterApi(OPEN_API_GEO_ONECALL, params, HttpMethod.GET);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new WeatherInfoDTO(objectMapper.readValue(strJson, WeatherInfoDTO.class), locationDTO);
    }

    public WeatherInfoDTO getWeatherByLocation(LocationDTO locationDTO, String language, String... units) throws JsonProcessingException {
        Map<String, Object> params = appendBaseParamWeather(locationDTO.getLat(), locationDTO.getLon(), language);
        params.put("exclude", EXCLUDES[1] + "," + EXCLUDES[2]);
        if ((units != null && units.length > 0) && units[0] != null) {
            params.put("units", units[0]);
        } else {
            params.put("units", UNITS[1]);
        }
        String strJson = connectAdapterApi.getDataFromAdapterApi(OPEN_API_GEO_ONECALL, params, HttpMethod.GET);
        JSONParser parser = new JSONParser();
        Map<String, Object> mapToPojo = new HashMap<>();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) parser.parse(strJson);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        JSONObject jsonCurrent = (JSONObject) jsonObject.get("current");
        mapToPojo.put("current", jsonCurrent);
        JSONArray jsonDaily = (JSONArray) jsonObject.get("daily");
        mapToPojo.put("daily", jsonDaily.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new WeatherInfoDTO(objectMapper.convertValue(mapToPojo, WeatherInfoDTO.class), locationDTO);
    }

    public String getCurrentInfoWeather(LocationDTO locationDTO, String language) {
        Map<String, Object> params = appendBaseParamWeather(locationDTO.getLat(), locationDTO.getLon(), language);
        params.put("units", UNITS[1]);
        return connectAdapterApi.getDataFromAdapterApi(OPEN_API_GEO_FORECAST, params, HttpMethod.GET);
    }

    private Map<String, Object> appendBaseParamWeather(float lat, float lon, String language) {
        Map<String, Object> params = new HashMap<>();
        params.put("lat", lat);
        params.put("lon", lon);
        params.put("appId", APP_ID_OPEN_WEATHER);
        if (Objects.nonNull(language)) {
            params.put("lang", language);
        }
        return params;
    }
}
