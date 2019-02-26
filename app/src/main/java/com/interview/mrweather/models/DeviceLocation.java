package com.interview.mrweather.models;

public class DeviceLocation {

    private String deviceLatitude;
    private String deviceLongitude;

    public DeviceLocation(String deviceLatitude, String deviceLongitude) {
        this.deviceLatitude = deviceLatitude;
        this.deviceLongitude = deviceLongitude;
    }

    public String getDeviceLatitude() {
        return deviceLatitude;
    }

    public void setDeviceLatitude(String deviceLatitude) {
        this.deviceLatitude = deviceLatitude;
    }

    public String getDeviceLongitude() {
        return deviceLongitude;
    }

    public void setDeviceLongitude(String deviceLongitude) {
        this.deviceLongitude = deviceLongitude;
    }
}
