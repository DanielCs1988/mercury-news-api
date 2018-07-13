package com.danielcs.mercurynews.models;

public class Geolocation {

    private String location;
    private double latitude;
    private double longitude;

    public Geolocation(String location, double latitude, double longitude) {
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
