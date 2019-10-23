package com.edynamix.learning.android.carservice.models;

public class CarBuilder {

    private int id;
    private String brand;
    private String model;
    private String colour;
    private int doorsCount;
    private int yearOfManufacture;
    private int carOwnerId;
    private String addedByUser;

    public CarBuilder() { }

    public CarBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public CarBuilder setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public CarBuilder setModel(String model) {
        this.model = model;
        return this;
    }

    public CarBuilder setColour(String colour) {
        this.colour = colour;
        return this;
    }

    public CarBuilder setDoorsCount(int doorsCount) {
        this.doorsCount = doorsCount;
        return this;
    }

    public CarBuilder setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
        return this;
    }

    public CarBuilder setCarOwnerId(int carOwnerId) {
        this.carOwnerId = carOwnerId;
        return this;
    }

    public CarBuilder setAddedByUser(String addedByUser) {
        this.addedByUser = addedByUser;
        return this;
    }

    public Car build() {
        return new Car(id, brand, model, colour, doorsCount, yearOfManufacture, carOwnerId, addedByUser);
    }
}
