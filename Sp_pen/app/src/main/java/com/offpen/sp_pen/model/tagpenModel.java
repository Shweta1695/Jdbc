package com.offpen.sp_pen.model;

public class tagpenModel {
    int id;
    String name,details,color;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public tagpenModel(int id, String name, String details, String color) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.color = color;
    }

    public tagpenModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
