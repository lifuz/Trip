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

    private static final int REQUEST_CODE = 110;


    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        //安卓6.0开始某些权限需要动态获取，以下就是动态获取授权的方法
        if (Build.VERSION.SDK_INT >= 23) {

            //判断权限是否已经授权
            int checkACCESSCOARSELOCATION = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            int checkACCESSFINELOCATION = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int checkREADPHONESTATE = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE);
            int checkREADSMS = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_SMS);
//
//            int checkREADCONTACTS = ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.READ_CONTACTS);

            int checkWRITEEXTERNALSTORAGE = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            int checkRECEIVESMS = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECEIVE_SMS);


            String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECEIVE_SMS};

            //如果没有授权，则调用授权方法
            if (checkACCESSCOARSELOCATION != PackageManager.PERMISSION_GRANTED
                    || checkACCESSFINELOCATION != PackageManager.PERMISSION_GRANTED
                    || checkREADPHONESTATE != PackageManager.PERMISSION_GRANTED
                    || checkREADSMS != PackageManager.PERMISSION_GRANTED
//                    || checkREADCONTACTS != PackageManager.PERMISSION_GRANTED
                    || checkWRITEEXTERNALSTORAGE != PackageManager.PERMISSION_GRANTED
                    || checkRECEIVESMS != PackageManager.PERMISSION_GRANTED
                    ) {

                //请求权限
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
                return;

            }


        }

    }

    @Subscriber(tag = "finish")
    private void distroy(String msg){
        Log.e(TAG,"收到关闭信息:" + msg);
        finish();
    }

    /**
     * 授权结果返回方法
     *
     * @param requestCode  授权请求的返回吗
     * @param permissions  请求那些权限
     * @param grantResults 每个权限的授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //根据授权的返回码，确定授权的调用者
        switch (requestCode) {
            case REQUEST_CODE:

                Log.e(TAG, grantResults[0] + "  " + grantResults[4] + "  " + PackageManager.PERMISSION_GRANTED);

                break;
        }

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
