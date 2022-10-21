package com.saltlux.deepsignal.feedcache.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DataListResponse<T>{
    private String result;
    private int result_code;
    private String requestId = "NULL";
    private List<T> data;
    public void setStatus(int result_code, String result){
            this.result_code = result_code;
            this.result = result;
            }
    public void setStatus(int result_code, String result, String requestId){
        this.result_code = result_code;
        this.result = result;
        this.requestId = requestId;
    }
    public DataListResponse(int result_code, String result) {
            this.result = result;
            this.result_code = result_code;
    }
    public DataListResponse(int result_code, String result, String requestId) {
        this.result = result;
        this.result_code = result_code;
        this.requestId = requestId;
    }
}
