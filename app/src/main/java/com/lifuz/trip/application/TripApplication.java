package com.lifuz.trip.application;

import android.app.Application;

import com.lifuz.trip.api.ApiService;

/**
 * app 上下文，初始化component
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/20 16:44
 */
public class TripApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .apiService(new ApiService())
                .build();

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
