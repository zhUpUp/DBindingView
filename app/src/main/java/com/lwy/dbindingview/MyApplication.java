package com.lwy.dbindingview;

import android.app.Application;

/**
 * Created by mac on 2018/4/23.
 */

public class MyApplication extends Application {

    private static MyApplication sMyApplication;

    public static MyApplication getMyApplication() {
        return sMyApplication;
    }

    @Override
    public void onCreate() {
        sMyApplication = this;
        super.onCreate();
    }
}