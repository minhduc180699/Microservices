package com.saltlux.deepsignal.web.exception;

public class AccountTemporaryException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AccountTemporaryException() {
        super("Account has not completed registration");
    }
}
