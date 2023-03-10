package com.saltlux.deepsignal.feedcache.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class DataResponse<T> {
    private String result;
    private String requestId = "NULL";
    private int result_code;
    private T data;
    public void setStatus(int result_code, String result){
        this.result_code = result_code;
        this.result = result;
    }

    public DataResponse(int result_code, String result) {
        this.result = result;
        this.result_code = result_code;
    }
    public void setStatus(int result_code, String result, String requestId){
        this.result_code = result_code;
        this.result = result;
        this.requestId = requestId;
    }

    public DataResponse(int result_code, String result, String requestId) {
        this.result = result;
        this.result_code = result_code;
        this.requestId = requestId;
    }
    public DataResponse failResponse(String requestId) {
        this.result = "failed";
        this.result_code = -1;
        this.requestId = requestId;
        return this;
    }
}
