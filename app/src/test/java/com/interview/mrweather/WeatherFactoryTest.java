package com.interview.mrweather;

import com.interview.mrweather.models.Weather;
import com.interview.mrweather.factories.WeatherFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WeatherFactoryTest {

    private String apiResponseJSON = "{\"coord\":{\"lon\":-122.09,\"lat\":37.39},\n" +
            "\"sys\":{\"type\":3,\"id\":168940,\"message\":0.0297,\"country\":\"US\",\"sunrise\":1427723751,\"sunset\":1427768967},\n" +
            "\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"Sky is Clear\",\"icon\":\"01n\"}],\n" +
            "\"base\":\"stations\",\n" +
            "\"main\":{\"temp\":285.68,\"humidity\":74,\"pressure\":1016.8,\"temp_min\":284.82,\"temp_max\":286.48},\n" +
            "\"wind\":{\"speed\":0.96,\"deg\":285.001},\n" +
            "\"clouds\":{\"all\":0},\n" +
            "\"dt\":1427700245,\n" +
            "\"id\":0,\n" +
            "\"name\":\"Mountain View\",\n" +
            "\"cod\":200}";

    WeatherFactory weatherFactory = new WeatherFactory();
    Weather weatherInfo;

    JSONObject apiJSONObject;

    @Before
    public void initWeatherInformation() throws Exception{
        apiJSONObject = new JSONObject(apiResponseJSON);
        weatherInfo = weatherFactory.extractWeatherInfo(apiResponseJSON);
    }

    @Test
    public void setWeatherInfoNotNull(){
        Assert.assertTrue(weatherInfo != null);
    }

    @Test
    public void extractWeatherMain_Should() throws Exception{

        JSONArray weatherArray = apiJSONObject.getJSONArray("weather");
        String wMain = weatherFactory.extractWeatherMain(weatherArray.getJSONObject(0));
        Assert.assertEquals("Clear", wMain );
    }

    @Test
    public void getTemperatureTest() throws Exception{

        JSONArray weatherArray = apiJSONObject.getJSONArray("weather");
        weatherFactory.extractWeatherMain(weatherArray.getJSONObject(0));
        Assert.assertTrue(weatherInfo.getMain().equalsIgnoreCase("Clear"));
    }
}
