package com.interview.mrweather.networking;


import com.interview.mrweather.models.DeviceLocation;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class WeatherApiRequest {

    private static final String TAG = "DailyForecastViewModel";
    private final String CURRENT_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private final String FORECAST_WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast";
    private final String API_KEY = "ae3091b1207e6e974801eda2ac8ff609";
    private final String TEMPERATURE_UNITS = "metric";

    public Request requestCurrentWeather(DeviceLocation deviceLocation) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(CURRENT_WEATHER_URL).newBuilder();
        return getRequestWithCommonHeaders(urlBuilder, deviceLocation);
    }

    public Request requestForecastWeather(DeviceLocation deviceLocation) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(FORECAST_WEATHER_URL).newBuilder();
        return getRequestWithCommonHeaders(urlBuilder, deviceLocation);
    }

    private Request getRequestWithCommonHeaders(HttpUrl.Builder urlBuilder, DeviceLocation deviceLocation) {

        urlBuilder.addQueryParameter("lat", String.valueOf(deviceLocation.getDeviceLatitude()));
        urlBuilder.addQueryParameter("lon", String.valueOf(deviceLocation.getDeviceLongitude()));
        urlBuilder.addQueryParameter("units", TEMPERATURE_UNITS);
        urlBuilder.addQueryParameter("appid", API_KEY);
        String URL = urlBuilder.build().toString();

        return new Request.Builder().url(URL).build();
    }
}
