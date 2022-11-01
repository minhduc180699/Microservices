package com.ds.dssearcher.response;

import com.ds.dssearcher.model.RequestBodyGetDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResponseDocument<T> {
    private String result;
    private Integer result_code;
    private String requestId;
    private T data;

    public ResponseDocument<?> getFailResponse(RequestBodyGetDocument requestBody) {
        return new ResponseDocument<>("failed", -1, requestBody.getRequestId(), null);
    }
}
