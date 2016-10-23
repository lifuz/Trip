package com.lifuz.trip.api.mine;

import com.lifuz.trip.enums.SelfState;
import com.lifuz.trip.module.common.SelfResult;
import com.lifuz.trip.module.mine.Token;
import com.lifuz.trip.module.mine.User;

import java.io.File;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * 用户相关信息
 * api 请求
 * <p>
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/20 16:43
 */
public interface UserApi {

    @Headers("Cache-Control: max-age=0")
    @Multipart
    @POST("appUser/{userId}/update")
    Observable<SelfState> updateHead(@Path("userId") Long userId, @Header("Authorization") String token
            , @Part MultipartBody.Part file);

    @Headers("Cache-Control: max-age=0")
    @GET("appUser/{userId}/user")
    Observable<SelfResult<User>> getUser(@Path("userId") Long userId, @Header("Authorization") String token);

    /**
     * 用户注册接口
     *
     * @param map 用户信息
     * @return 注册结果
     */
    @Headers("Cache-Control: max-age=0")
    @POST("appUser/register")
    Observable<SelfResult<SelfState>> register(@QueryMap Map<String, String> map);

    /**
     * 手机号登录接口
     *
     * @param phone  手机号
     * @param passwd 密码
     * @return 登录结果
     */
    @Headers("Cache-Control: max-age=0")
    @GET("appUser/{phone}/phoneLogin")
    Observable<SelfResult<Token>> phoneLogin(
            @Path("phone") Long phone, @Query("passwd") String passwd);

    /**
     * id号登录接口
     *
     * @param userId
     * @return
     */
    @Headers("Cache-Control: max-age=0")
    @GET("appUser/{userId}/userId")
    Observable<SelfResult<Token>> userId(@Path("userId") String userId);

}
