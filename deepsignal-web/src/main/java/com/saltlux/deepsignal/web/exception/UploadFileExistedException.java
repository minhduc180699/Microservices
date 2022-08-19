package com.saltlux.deepsignal.web.exception;

public class UploadFileExistedException extends UploadException {

    public UploadFileExistedException(String message) {
        super(message);
    }

    public UploadFileExistedException(String message, Throwable cause) {
        super(message, cause);
    }
}
