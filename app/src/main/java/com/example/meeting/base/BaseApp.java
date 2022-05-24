package com.example.meeting.base;

import android.app.Application;

import com.example.framework.Framework;

public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Framework.getFramework().initFramework(this);
    }
}
