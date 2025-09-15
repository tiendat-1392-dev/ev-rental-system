package com.webserver.evrentalsystem.exception;

public class UserIsBlockedException extends RuntimeException {
    public UserIsBlockedException(String message) {
        super(message);
    }
}
