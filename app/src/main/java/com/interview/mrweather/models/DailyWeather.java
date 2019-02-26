package com.interview.mrweather.models;

import lombok.Data;

@Data
public class DailyWeather {

    private String day;
    private String temperature;
    private int icon;

    public DailyWeather(String day, String temperature, int icon) {
        this.day = day;
        this.temperature = temperature;
        this.icon = icon;
    }
}
