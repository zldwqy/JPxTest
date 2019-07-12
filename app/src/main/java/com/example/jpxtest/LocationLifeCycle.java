package com.example.jpxtest;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class LocationLifeCycle implements LifecycleObserver {
    public static final String TAG = "LocationLifeCycle";

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void startLocton(){
        Log.d(TAG, "startLocton: ");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stopLocation(){
        Log.d(TAG, "stopLocation: ");
    }
}
