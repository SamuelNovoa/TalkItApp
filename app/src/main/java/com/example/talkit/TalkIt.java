package com.example.talkit;

import android.app.Application;

import android.content.Context;

public class TalkIt extends Application {
    public static final String BACKEND_URL = "http://10.0.2.2:8000/api/";

    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
