package com.interview.mrweather.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.interview.mrweather.R;
import com.interview.mrweather.models.DailyWeather;

import java.util.List;

public class DailyForecastViewHolder extends RecyclerView.ViewHolder {


    public TextView tvDay;
    public TextView tvTemperature;
    public ImageView imgIcon;


    public DailyForecastViewHolder(@NonNull View itemView) {
        super(itemView);

        tvDay = itemView.findViewById(R.id.tv_day);
        tvTemperature = itemView.findViewById(R.id.tv_daily_temperature);
        imgIcon = itemView.findViewById(R.id.img_daily_status);
    }
}
