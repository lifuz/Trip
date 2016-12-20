package com.lifuz.trip.api;

import com.lifuz.trip.api.mine.UserApi;
import com.lifuz.trip.application.TripApplication;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求封装
 *
 * module 类
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/20 16:41
 */
@Module
public class ApiService {

//    private static final String BASE_URL = "http://121.41.64.237/self-web/";

    private static final String BASE_URL = "http://192.168.0.108:8088/self-web/";

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {

        File file = new File("/sdcard/trip/cache");
        if (!file.exists()) {
            file.mkdirs();
        }

        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addNetworkInterceptor(new CaheInterceptor())
                .cache(new Cache(file, 1024 * 1024 * 100))
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60,TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient){

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    @Provides
    @Singleton
    Picasso providePicasso(OkHttpClient okHttpClient) {
        Picasso picasso = new Picasso.Builder(TripApplication.getContext())
                .downloader(new OkHttpDownLoader(okHttpClient))
                .build();

        Picasso.setSingletonInstance(picasso);
        return picasso;
    }

    @Provides
    @Singleton
    public UserApi provideUserApi(Retrofit retrofit){

        return retrofit.create(UserApi.class);

    }

}
