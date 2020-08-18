package com.example.registration;

public class LocationHelper {
    private double Latitude;
    private double Longitude;

    public LocationHelper(double  latitude ,double longitude){
        Latitude = latitude;
        Longitude = longitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }
}
