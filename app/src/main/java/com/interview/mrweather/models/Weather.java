package com.interview.mrweather.models;

import lombok.Data;

@Data
public class Weather {

    private String main;
    private String temperature;
    private String description;
    private String locationName;
    private String minimumTemperature;
    private String maximumTemperature;
}
