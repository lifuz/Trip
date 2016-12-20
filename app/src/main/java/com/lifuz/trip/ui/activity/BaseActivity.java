package com.lifuz.trip.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/**
 * activity 基类
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/20 16:55
 */
public class BaseActivity extends AppCompatActivity {



    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);



    }

    @Subscriber(tag = "finish")
    private void distroy(String msg){
        Log.e(TAG,"收到关闭信息:" + msg);
        finish();
    }



    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
