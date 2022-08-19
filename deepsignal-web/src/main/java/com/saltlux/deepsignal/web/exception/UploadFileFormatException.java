package com.saltlux.deepsignal.web.exception;

public class UploadFileFormatException extends UploadException {

    public UploadFileFormatException(String message) {
        super(message);
    }

    public UploadFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
