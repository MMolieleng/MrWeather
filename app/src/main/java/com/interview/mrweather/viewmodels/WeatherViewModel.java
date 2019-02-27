package com.interview.mrweather.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.interview.mrweather.models.DeviceLocation;
import com.interview.mrweather.models.Weather;
import com.interview.mrweather.factories.WeatherFactory;
import com.interview.mrweather.networking.WeatherApiRequest;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherViewModel extends ViewModel {

    private static final String TAG = "WeatherViewModel";
    private OkHttpClient okHttpClient;

    private MutableLiveData<Weather> weatherMutableLiveData;

    public LiveData<Weather> getWeather(DeviceLocation deviceLocation) {
        if (weatherMutableLiveData == null) {
            weatherMutableLiveData = new MutableLiveData<>();
            if (deviceLocation != null)
                initWeather(deviceLocation);
        }
        return weatherMutableLiveData;
    }

    public void initWeather(DeviceLocation deviceLocation) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        requestWeather(deviceLocation);
    }

    private void requestWeather(DeviceLocation deviceLocation) {

        Request apiRequest = new WeatherApiRequest().requestCurrentWeather(deviceLocation);
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
                            Weather weather = new WeatherFactory().extractWeatherInfo(apiResponse);
                            if (weather != null)
                                weatherMutableLiveData.postValue(weather);
                        }
                    }
                });
    }
}
