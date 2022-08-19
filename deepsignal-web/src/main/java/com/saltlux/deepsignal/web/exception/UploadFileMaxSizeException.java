package com.saltlux.deepsignal.web.exception;

public class UploadFileMaxSizeException extends UploadException {

    public UploadFileMaxSizeException(String message) {
        super(message);
    }

    public UploadFileMaxSizeException(String message, Throwable cause) {
        super(message, cause);
    }
}
