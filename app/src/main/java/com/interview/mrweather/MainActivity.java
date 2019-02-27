package com.interview.mrweather;

import android.Manifest;
import android.animation.ValueAnimator;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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
import com.interview.mrweather.models.DeviceLocation;
import com.interview.mrweather.models.Weather;
import com.interview.mrweather.viewmodels.CurrentLocationListener;
import com.interview.mrweather.viewmodels.DailyForecastViewModel;
import com.interview.mrweather.viewmodels.WeatherViewModel;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
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
    private static DeviceLocation DEVICE_LOCATION = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JodaTimeAndroid.init(this);

        dailyWeatherList = new ArrayList<>();
        enableFullScreen();
        init();
    }

    public void init() {
        if (isOnline()) {
            initUtils();
            initUIComponents();
            initViewModel();
            updateUI();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No network, please ensure that your're connected to the internet")
                    .setTitle("Connection");
            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void enableFullScreen() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
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

        weatherVM.getWeather(DEVICE_LOCATION).observe(this, weather -> {
            Log.i(TAG, "updateUI: ");
            if (weather != null) {
                updateTheme(weather);
                updateMainTemperatureDisplay(weather.getTemperature());
                updateOtherViews(weather);
            }
        });

        forecastVM.getDailyWeatherForecast(DEVICE_LOCATION).observe(this, tmpDailyWeatherList -> {
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
                scrollView.setBackgroundResource(R.color.colorRainy);
                break;
            case "Clouds":
            case "Snow":
                imgHeader.setImageResource(R.drawable.forest_cloudy);
                parentLayout.setBackgroundResource(R.color.colorCloudy);
                scrollView.setBackgroundResource(R.color.colorCloudy);
                break;
            case "Clear":
                imgHeader.setImageResource(R.drawable.forest_sunny);
                parentLayout.setBackgroundResource(R.color.colorSunny);
                break;
            default:
                imgHeader.setImageResource(R.drawable.forest_sunny);
                parentLayout.setBackgroundResource(R.color.colorSunny);
                scrollView.setBackgroundResource(R.color.colorSunny);
                break;
        }
    }

    // I have added animation for the main temperature textview
    public void updateMainTemperatureDisplay(String tmpString) {
        if (tmpString != null)
            tvMainTemperature.setText(tmpString + "°");
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

    public void initUtils() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            getLocationUpdates();
        }
    }

    private void getLocationUpdates() {
        CurrentLocationListener.getInstance(getApplicationContext()).observe(this, new Observer<Location>() {
            @Override
            public void onChanged(@Nullable Location location) {
                if (location != null) {
                    Log.d(MainActivity.class.getSimpleName(),
                            "Location Changed " + location.getLatitude() + " : " + location.getLongitude());
//                    Toast.makeText(MainActivity.this, "Location Changed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onChanged:");
                    MainActivity.DEVICE_LOCATION = new DeviceLocation(location.getLatitude(), location.getLongitude());
                    if (weatherVM != null && forecastVM != null) {
                        weatherVM.initWeather(new DeviceLocation(location.getLatitude(), location.getLongitude()));
                        forecastVM.initDailyWeatherForecast(new DeviceLocation(location.getLatitude(), location.getLongitude()));
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getLocationUpdates();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}
