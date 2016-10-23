package com.lifuz.trip.application;

import com.lifuz.trip.api.ApiService;
import com.lifuz.trip.api.mine.UserApi;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 *  总component组件
 *
 *  component 部分
 *
 *  提供了网络请求的封装
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/20 16:45
 */
@Singleton
@Component ( modules = {ApiService.class})
public interface AppComponent {

    OkHttpClient getClient();

    Retrofit getRetrofit();

    Picasso getPicasso();

    UserApi getUserApi();

}
