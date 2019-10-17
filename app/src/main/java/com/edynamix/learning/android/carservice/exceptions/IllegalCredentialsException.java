package com.edynamix.learning.android.carservice.exceptions;

public class IllegalCredentialsException extends LoginFailedException {

    public IllegalCredentialsException(String message) {
        super(message);
    }
}
