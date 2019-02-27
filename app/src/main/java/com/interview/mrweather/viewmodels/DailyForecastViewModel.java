package com.interview.mrweather.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.interview.mrweather.R;
import com.interview.mrweather.factories.WeatherFactory;
import com.interview.mrweather.models.DailyWeather;
import com.interview.mrweather.models.DeviceLocation;
import com.interview.mrweather.models.Weather;
import com.interview.mrweather.networking.WeatherApiRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DailyForecastViewModel extends ViewModel {

    private static final String TAG = "DailyForecastViewModel";
    private OkHttpClient okHttpClient;

    private MutableLiveData<List<DailyWeather>> dailyWeatherMutableLiveData;

    public LiveData<List<DailyWeather>> getDailyWeatherForecast(DeviceLocation deviceLocation) {
        if (dailyWeatherMutableLiveData == null) {
            dailyWeatherMutableLiveData = new MutableLiveData<>();
            if (deviceLocation != null)
                initDailyWeatherForecast(deviceLocation);
        }
        return dailyWeatherMutableLiveData;
    }

    public void initDailyWeatherForecast(DeviceLocation deviceLocation) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        requestDailyWeatherForecast(deviceLocation);
    }

    private void requestDailyWeatherForecast(DeviceLocation deviceLocation) {

        Request apiRequest = new WeatherApiRequest().requestForecastWeather(deviceLocation);
        okHttpClient.newCall(apiRequest)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        } else {
                            String apiResponse = response.body().string();
                            Log.i(TAG, "onResponse: " + apiResponse);
                            List<DailyWeather> weatherList = new WeatherFactory().dailyForecast(apiResponse);
                            if (weatherList != null)
                                dailyWeatherMutableLiveData.postValue(weatherList);
                        }
                    }
                });
    }
}
