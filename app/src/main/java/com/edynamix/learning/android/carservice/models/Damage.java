package com.edynamix.learning.android.carservice.models;

public class Damage {

    public static int counter = 0;

    public int id;
    public String imageSource;
    public String description;
    public float xPositionOnCarTemplate;
    public float yPositionOnCarTemplate;

    public Damage(String imageSource, String description, float xPositionOnCarTemplate, float yPositionOnCarTemplate) {
        this.id = ++counter;
        this.imageSource = imageSource;
        this.description = description;
        this.xPositionOnCarTemplate = xPositionOnCarTemplate;
        this.yPositionOnCarTemplate = yPositionOnCarTemplate;
    }
}
