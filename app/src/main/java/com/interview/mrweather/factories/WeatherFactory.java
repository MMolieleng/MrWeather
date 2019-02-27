package com.interview.mrweather.factories;

import com.interview.mrweather.R;
import com.interview.mrweather.models.DailyWeather;
import com.interview.mrweather.models.Weather;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeatherFactory {

    public Weather extractWeatherInfo(String apiResponse) {

        Weather weatherInfo = null;
        try {
            weatherInfo = new Weather();
            JSONObject apiResponseObject = new JSONObject(apiResponse);
            if (apiResponseObject.has("weather")) {
                JSONArray weatherArray = apiResponseObject.getJSONArray("weather");

                // Weather array always has size of 1
                JSONObject wInfo = weatherArray.getJSONObject(0);
                weatherInfo.setMain(extractWeatherMain(wInfo));
                weatherInfo.setDescription(extractWeatherDescription(wInfo));
            }

            if (apiResponseObject.has("main")) {
                JSONObject mainObject = apiResponseObject.getJSONObject("main");
                weatherInfo.setTemperature(extractCurrentTemperature(mainObject));
                weatherInfo.setMinimumTemperature(extractMinimumTemperature(mainObject));
                weatherInfo.setMaximumTemperature(extractMaximumTemperature(mainObject));
            }
            return weatherInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Extract current temperature from API response
    public String extractCurrentTemperature(JSONObject weatherObject) throws JSONException {
        return weatherObject.has("temp") ? String.valueOf(weatherObject.getInt("temp")) : null;
    }

    // Extract Minimum temperature from API response
    public String extractMinimumTemperature(JSONObject weatherObject) throws JSONException {
        return weatherObject.has("temp_min") ? String.valueOf(weatherObject.getInt("temp_min")) : null;
    }

    // Extract Maximum temperature from API response
    public String extractMaximumTemperature(JSONObject weatherObject) throws JSONException {
        return weatherObject.has("temp_max") ? String.valueOf(weatherObject.getInt("temp_max")) : null;
    }

    // Get Weather Main Status
    public String extractWeatherMain(JSONObject weatherObject) throws JSONException {
        return weatherObject.has("main") ? weatherObject.getString("main") : null;
    }

    // Get Weather Description
    public String extractWeatherDescription(JSONObject weatherObject) throws JSONException {
        return weatherObject.has("description") ? weatherObject.getString("description") : null;
    }

    public List<DailyWeather> dailyForecast(String apiResponse) {

        List<DailyWeather> dailyWeatherList = new ArrayList<>();
        try {
            JSONObject apiJSONObject = new JSONObject(apiResponse);
            if (apiJSONObject.has("list")) {
                JSONArray array = apiJSONObject.getJSONArray("list");
                for (int i = 0; i < array.length(); i++) {
                    String currentDay = array.getJSONObject(i).getString("dt_txt");
                    if (currentDay.endsWith("12:00:00")) {
                        Weather weather = extractWeatherInfo(array.getJSONObject(i).toString());
                        DailyWeather dailyWeather = createDailyWeather(weather, currentDay);
                        dailyWeatherList.add(dailyWeather);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dailyWeatherList;
    }

    private DailyWeather createDailyWeather(Weather weather, String currentDay) {

        int icon = R.drawable.clear_icon;
        switch (weather.getMain()) {
            case "Clear":
                icon = R.drawable.clear_icon;
                break;
            case "Rain":
            case "Clouds":
                icon = R.drawable.partlysunny_icon;
                break;
            default:
                icon = R.drawable.clear_icon;
        }

        LocalDate localDate = LocalDate.parse(currentDay.split(" ")[0]);
        String [] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday", "Sunday"};
        return new DailyWeather(days[localDate.getDayOfWeek() % days.length], weather.getTemperature(), icon);
    }
}
