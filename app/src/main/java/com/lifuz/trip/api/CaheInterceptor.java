package com.lifuz.trip.api;

import com.lifuz.trip.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * okhttp拦截器
 *
 * @author: 李富
 * @email: lifuzz@163.com
 * @time: 2016/10/23 11:21
 */

public class CaheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        //如果没有网络，则启用 FORCE_CACHE
        if (!NetworkUtils.isNetworkConnected()) {
            request = request
                    .newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response originalResponse = chain.proceed(request);
        return originalResponse
                .newBuilder()
                //在这里生成新的响应并修改它的响应头
                .header("Cache-Control", "public,max-age=3600")
                .removeHeader("Pragma").build();

    }
}
