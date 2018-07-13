package com.danielcs.mercurynews.models;

public class Weather {

    private String location;
    private String summary;
    private String icon;
    private double temperature;

    public Weather(String location, String summary, String icon, double temperature) {
        this.location = location;
        this.summary = summary;
        this.icon = icon;
        this.temperature = (temperature - 32) * 5 / 9;
    }

    public String getLocation() {
        return location;
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }

    public double getTemperature() {
        return temperature;
    }
}
