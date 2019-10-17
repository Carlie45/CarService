package com.edynamix.learning.android.carservice.exceptions;

public class NoSuchUserException extends LoginFailedException {

    public NoSuchUserException(String message) {
        super(message);
    }
}
