package com.ds.dssearcher.response;

import com.ds.dssearcher.model.RequestBodyGetDocument;
import com.ds.dssearcher.model.RequestBodyGetListDoc;
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
public class ResponseListData<T> {
    private String result;
    private int result_code;
    private String requestId;
    private Integer currentPage;
    private Long totalItems;
    private Integer totalPages;
    private List<T> data;

    public ResponseListData<?> getFailResponse(RequestBodyGetListDoc requestBody) {
        return new ResponseListData<>("failed", -1, requestBody.getRequestId(), 0,0L,0,null);
    }
}
