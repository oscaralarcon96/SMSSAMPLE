package com.example.sms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.os.SystemClock;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(4000);
    }
}