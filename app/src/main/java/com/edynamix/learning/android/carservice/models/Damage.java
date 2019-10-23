package com.edynamix.learning.android.carservice.models;

public class Damage {

    public int id;
    public String imageSource;
    public String description;
    public float xPositionOnCarTemplate;
    public float yPositionOnCarTemplate;

    public Damage(int id, String imageSource, String description, float xPositionOnCarTemplate, float yPositionOnCarTemplate) {
        this.id = id;
        this.imageSource = imageSource;
        this.description = description;
        this.xPositionOnCarTemplate = xPositionOnCarTemplate;
        this.yPositionOnCarTemplate = yPositionOnCarTemplate;
    }
}
