package com.saltlux.deepsignal.web.service.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse {

    private Boolean success;
    private String message;
    private String code;
    private String feild;
    private Integer loginFailedCount;

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponse(Boolean success, String message, String code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

    public ApiResponse(Boolean success, String message, String code, Integer loginFailedCount) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.loginFailedCount = loginFailedCount;
    }

    public ApiResponse(Boolean success, String message, String code, String feild) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.feild = feild;
    }

    public ApiResponse(Boolean success) {
        this.success = success;
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResult = "CHUA KHOI TAO";
        try {
            jsonResult = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            jsonResult = e.getMessage();
            System.err.println(e);
        } catch (Exception e) {
            jsonResult = e.getMessage();
            System.err.println(e);
        }
        return jsonResult;
    }
}
