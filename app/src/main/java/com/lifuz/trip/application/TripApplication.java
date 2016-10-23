package com.lifuz.trip.application;

import android.app.Application;
import android.content.Context;

import com.lifuz.trip.R;
import com.lifuz.trip.api.ApiService;

import cn.smssdk.SMSSDK;

/**
 * app 上下文，初始化component
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/20 16:44
 */
public class TripApplication extends Application {

    private AppComponent appComponent;

    private static Context context = null;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        SMSSDK.initSDK(this, getString(R.string.sms_appkey), getString(R.string.sms_appsecret));

        appComponent = DaggerAppComponent.builder()
                .apiService(new ApiService())
                .build();

    }

    public static Context getContext() {
        return context;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
