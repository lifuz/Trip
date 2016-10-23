package com.lifuz.trip.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.lifuz.trip.application.TripApplication;

/**
 * 网络监测工具类
 *
 * @author: 李富
 * @email: lifuzz@163.com
 * @time: 2016/10/23 11:25
 */

public class NetworkUtils {

    /**
     * 是否有网络
     * @return
     */
    public static boolean isNetworkConnected()
    {
        Context context = TripApplication.getContext();
        if (context != null)
        {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null)
            {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 是否连上wifi
     * @return 是否连上wifi
     */
    static public boolean isWifiConnected()
    {

        ConnectivityManager mConnectivityManager = (ConnectivityManager) TripApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWiFiNetworkInfo != null)
        {
            return mWiFiNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 是否有数据网络
     *
     * @param context 上下文
     * @return 是否
     */
    static public boolean isMobileConnected(Context context)
    {
        if (context != null)
        {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null)
            {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
