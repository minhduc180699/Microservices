package com.saltlux.deepsignal.feedcache.exception;

import com.saltlux.deepsignal.feedcache.dto.ResultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralException {
    private ResultResponse response = new ResultResponse(-1, "failed");
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> exception(Exception e){
        return ResponseEntity.ok(response);
    }
}
