package com.edynamix.learning.android.carservice.models;

public class User {

    public String email;
    public String passwordHash;

    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

}
