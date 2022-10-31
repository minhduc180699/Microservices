package com.saltlux.deepsignal.feedcache.dto.response;

import com.saltlux.deepsignal.feedcache.model.request.RequestBodyGetDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResponseDocument {
    private String result;
    private Integer result_code;
    private String requestId;
    private Object data;

    public ResponseDocument getFailResponse(RequestBodyGetDocument requestBody) {
        return new ResponseDocument("failed", -1, requestBody.getRequestId(), null);
    }

    public void success() {
        this.result = "success";
        this.result_code = 0;
    }
}
