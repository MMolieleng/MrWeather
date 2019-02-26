package com.interview.mrweather;

import android.animation.ValueAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.interview.mrweather.adapters.DailyForecastAdapter;
import com.interview.mrweather.models.DailyWeather;
import com.interview.mrweather.models.Weather;
import com.interview.mrweather.viewmodels.DailyForecastViewModel;
import com.interview.mrweather.viewmodels.WeatherViewModel;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private WeatherViewModel weatherVM;
    private DailyForecastViewModel forecastVM;
    private ConstraintLayout parentLayout;
    private ImageView imgHeader;
    private TextView tvMainTemperature;
    private TextView tvMainStatus;
    private TextView tvMinTemperature;
    private TextView tvMaxTemperature;
    private TextView tvCurrentTemperature;

    private RecyclerView recyclerView;
    private DailyForecastAdapter forecastAdapter;
    private List<DailyWeather> dailyWeatherList;

    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JodaTimeAndroid.init(this);

        dailyWeatherList = new ArrayList<>();
        enableFullScreen();
        initUIComponents();
        initViewModel();
        updateUI();
    }

    public void enableFullScreen() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void initUIComponents() {

        scrollView = findViewById(R.id.parentScrollView);
//        scrollView.setScaleY(0);
        parentLayout = findViewById(R.id.parentConstraintLayout);
        imgHeader = findViewById(R.id.img_header);
        tvMainStatus = findViewById(R.id.tv_main_status);
        tvMainTemperature = findViewById(R.id.tv_main_temperature);

        tvMinTemperature = findViewById(R.id.min_tv);
        tvMaxTemperature = findViewById(R.id.max_tv);
        tvCurrentTemperature = findViewById(R.id.current_tv);

        // initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearVertical =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearVertical);
        forecastAdapter = new DailyForecastAdapter(dailyWeatherList);
        recyclerView.setAdapter(forecastAdapter);
    }

    private void initViewModel() {
        weatherVM = ViewModelProviders.of(this).get(WeatherViewModel.class);
        forecastVM = ViewModelProviders.of(this).get(DailyForecastViewModel.class);
    }

    private void updateUI() {

        weatherVM.getWeather().observe(this, weather -> {
            Log.i(TAG, "updateUI: ");
            if (weather != null) {
                updateTheme(weather);
                updateMainTemperatureDisplay(weather.getTemperature());
                updateOtherViews(weather);
            }
        });

        forecastVM.getDailyWeatherForecast().observe(this, tmpDailyWeatherList -> {
            if (tmpDailyWeatherList.size() > 0) {
                // Initialize Adapter
                dailyWeatherList.clear();
                dailyWeatherList.addAll(tmpDailyWeatherList);
                forecastAdapter.notifyDataSetChanged();
            }
        });
    }

    public void updateTheme(Weather weather) {

        switch (weather.getMain()) {
            case "Rain":
            case "Drizzle":
            case "Thunderstorm":
                imgHeader.setImageResource(R.drawable.forest_rainy);
                parentLayout.setBackgroundResource(R.color.colorRainy);
                break;
            case "Clouds":
            case "Snow":
                imgHeader.setImageResource(R.drawable.forest_cloudy);
                parentLayout.setBackgroundResource(R.color.colorCloudy);
                break;
            case "Clear":
                imgHeader.setImageResource(R.drawable.forest_sunny);
                parentLayout.setBackgroundResource(R.color.colorSunny);
                break;
            default:
                imgHeader.setImageResource(R.drawable.forest_sunny);
                parentLayout.setBackgroundResource(R.color.colorSunny);
                break;
        }
    }

    // I have added animation for the main temperature textview
    public void updateMainTemperatureDisplay(String tmpString) {

        int temperature = (int) Integer.parseInt(tmpString);
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, temperature);
        animator.addUpdateListener(animation -> tvMainTemperature.setText(String.valueOf(animation.getAnimatedValue()) + "°"));
        animator.setDuration(1500);
        animator.start();
    }

    public void updateOtherViews(Weather weather) {

        tvMinTemperature.setText(weather.getMinimumTemperature() + "°");
        tvCurrentTemperature.setText(weather.getTemperature() + "°");
        tvMaxTemperature.setText(weather.getMaximumTemperature() + "°");

        switch (weather.getMain()) {
            case "Clouds":
                tvMainStatus.setText("Cloudy");
                break;
            case "Clear":
                tvMainStatus.setText("Sunny");
                break;
            case "Rain":
                tvMainStatus.setText("Rainy");
                break;
            default:
                tvMainStatus.setText(weather.getMain());
        }
    }
}
