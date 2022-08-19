package com.saltlux.deepsignal.web.exception;

public class AccountUnconfirmException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AccountUnconfirmException() {
        super("Account already unconfirm");
    }
}
