package com.saltlux.deepsignal.feedcache.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResultResponse{
    private String result;
    private String requestId = "NULL";
    private int result_code;
    public void setStatus(int result_code, String result){
            this.result_code = result_code;
            this.result = result;
            }

    public ResultResponse(int result_code, String result) {
            this.result = result;
            this.result_code = result_code;
    }
    public void setStatus(int result_code, String result, String requestId){
        this.result_code = result_code;
        this.result = result;
        this.requestId = requestId;
    }

    public ResultResponse(int result_code, String result, String requestId) {
        this.result = result;
        this.result_code = result_code;
        this.requestId = requestId;
    }
}
