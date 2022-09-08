package com.offpen.sp_pen.model;

public class viewpenmodel {

    String name,type,batteryPer;



    public viewpenmodel(String name, String type, String batteryPer) {
        this.name = name;
        this.type = type;
        this.batteryPer = batteryPer;
    }

    public viewpenmodel() {
    }

    public String  getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBatteryPer() {
        return batteryPer;
    }

    public void setBatteryPer(String batteryPer) {
        this.batteryPer = batteryPer;
    }
}
