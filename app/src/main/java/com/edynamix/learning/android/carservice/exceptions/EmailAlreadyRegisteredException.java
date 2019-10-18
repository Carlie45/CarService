package com.edynamix.learning.android.carservice.exceptions;

public class EmailAlreadyRegisteredException extends RegistrationFailedException {

    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
