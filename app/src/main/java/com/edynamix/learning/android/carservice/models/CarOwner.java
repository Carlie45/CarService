package com.edynamix.learning.android.carservice.models;

public class CarOwner {

    public long id;
    public String firstName;
    public String lastName;
    public String address;
    public String phone;
    public String email;

    CarOwner(long id, String firstName, String lastName, String address, String phone, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String toString() {
        return "CarOwner{" +
                "id='" + id + '\'' +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
