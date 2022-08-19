package com.saltlux.deepsignal.web.exception;

public class NotificationTypeNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotificationTypeNotFoundException() {
        super("Notification type does not exist. Please add new notification type to database, then implement your notification type");
    }
}
