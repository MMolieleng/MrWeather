package com.interview.mrweather.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.interview.mrweather.R;
import com.interview.mrweather.models.DailyWeather;
import com.interview.mrweather.viewholders.DailyForecastViewHolder;

import java.util.List;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastViewHolder> {

    private List<DailyWeather> dailyWeatherList;

    public DailyForecastAdapter(List<DailyWeather> dailyWeatherList) {
        this.dailyWeatherList = dailyWeatherList;
    }

    @NonNull
    @Override
    public DailyForecastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.daily_weather_row, viewGroup, false);
        return new DailyForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyForecastViewHolder dailyWeatherViewHolder, int position) {

        DailyWeather weather = dailyWeatherList.get(position);
        dailyWeatherViewHolder.imgIcon.setImageResource(weather.getIcon());
        dailyWeatherViewHolder.tvTemperature.setText(weather.getTemperature()+ "°");
        dailyWeatherViewHolder.tvDay.setText(weather.getDay());
    }

    @Override
    public int getItemCount() {
        return dailyWeatherList.size();
    }
}
