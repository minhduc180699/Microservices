package com.saltlux.deepsignal.web.exception;

public class AccountAlreadyNotExistException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AccountAlreadyNotExistException() {
        super("Account already not exist");
    }
}
