package com.interview.mrweather.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.interview.mrweather.models.DeviceLocation;

import okhttp3.OkHttpClient;

public class DeviceLocationViewModel extends ViewModel {

    private static final String TAG = "DeviceLocationViewModel";

    private MutableLiveData<DeviceLocation> deviceLocationMutableLiveData;

    public LiveData<DeviceLocation> getDeviceLocation(){
        if (deviceLocationMutableLiveData == null){
            deviceLocationMutableLiveData = new MutableLiveData<>();
            initLocation();
        }
        return deviceLocationMutableLiveData;
    }

    public void initLocation(){
        DeviceLocation deviceLocation = new DeviceLocation("-26.061110","28.101210");
        deviceLocationMutableLiveData.postValue(deviceLocation);
    }
}
