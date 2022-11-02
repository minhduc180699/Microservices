package com.ds.dssearcher.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObject {
    private String result;
    private int result_code;
    private Object data;

    public static ResponseObject failResponse = new ResponseObject("failed", -1, null);

}