package com.edynamix.learning.android.carservice.models;

public class CarOwnerBuilder {

    private int id;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String email;

    public CarOwnerBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public CarOwnerBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public CarOwnerBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CarOwnerBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public CarOwnerBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public CarOwnerBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public CarOwner build() {
        return new CarOwner(id, firstName, lastName, address, phone, email);
    }
}
