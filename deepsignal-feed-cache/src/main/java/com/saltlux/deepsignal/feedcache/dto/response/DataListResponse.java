package com.saltlux.deepsignal.feedcache.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataListResponse<T>{
    private String result;
    private int result_code;
    private String requestId = "NULL";
    private String currentPage;
    private String totalItems;
    private String totalPages;
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
    public void mergeResponse(DataListResponse response){
        this.setStatus(response.result_code, response.result,response.requestId);
        this.currentPage = response.currentPage;
        this.totalItems = response.totalItems;
        this.totalPages = response.totalPages;
    }
}
