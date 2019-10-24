package com.edynamix.learning.android.carservice;

import android.app.Application;
import android.content.res.Resources;

public class App extends Application {

    private static App mInstance;
    private static Resources res;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        res = getResources();
    }

    public static Resources getRes() {
        return res;
    }
}
