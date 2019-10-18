package com.edynamix.learning.android.carservice.models;

import java.util.ArrayList;
import java.util.List;

public class Car {

    public String brand;
    public String model;
    public String colour;
    public int doorsCount;
    public int yearOfManufacture;

    public int carOwnerId;
    public String addedByUser;

    public List<Damage> damageList;

    Car(String brand, String model, String colour, int doorsCount, int  yearOfManufacture, int carOwnerId, String addedByUser) {
        this.brand = brand;
        this.model = model;
        this.colour = colour;
        this.doorsCount = doorsCount;
        this.yearOfManufacture = yearOfManufacture;
        this.carOwnerId = carOwnerId;
        this.addedByUser = addedByUser;
        this.damageList = new ArrayList<>();
        damageList.add(new Damage(""));
        damageList.add(new Damage(""));
        damageList.add(new Damage(""));
        damageList.add(new Damage(""));
        damageList.add(new Damage(""));
        damageList.add(new Damage(""));
        damageList.add(new Damage(""));
        damageList.add(new Damage(""));
        damageList.add(new Damage(""));
        damageList.add(new Damage(""));
    }

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", colour='" + colour + '\'' +
                ", doorsCount=" + doorsCount +
                ", yearOfManufacture=" + yearOfManufacture +
                ", carOwnerId=" + carOwnerId +
                ", addedByUser='" + addedByUser + '\'' +
                '}';
    }

}
