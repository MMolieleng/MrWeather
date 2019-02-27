package com.interview.mrweather.viewmodels;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class CurrentLocationListener extends LiveData<Location> {

    private static final long UPDATES_INTERVAL = 1000 * 60;
    private static final long FASTEST_UPDATE_INTERVAL = 1000 * 30;
    private static CurrentLocationListener instance;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private static Context appContext;

    public static CurrentLocationListener getInstance(Context appContext) {
        if (instance == null) {
            CurrentLocationListener.appContext = appContext;
            instance = new CurrentLocationListener(appContext);
        }
        return instance;
    }

    @SuppressLint("MissingPermission")
    private CurrentLocationListener(Context appContext) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null)
                    setValue(location);
            }
        });
        createLocationRequest();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATES_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onActive() {
        super.onActive();
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (location != null)
                    setValue(location);
            }
        }
    };

    @Override
    protected void onInactive() {
        super.onInactive();
        if (mLocationCallback != null)
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }
}

