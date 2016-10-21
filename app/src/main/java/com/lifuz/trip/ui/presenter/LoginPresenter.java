package com.lifuz.trip.ui.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.lifuz.trip.api.mine.UserApi;
import com.lifuz.trip.module.common.SelfResult;
import com.lifuz.trip.module.mine.Token;
import com.lifuz.trip.ui.activity.LoginActivity;
import com.lifuz.trip.utils.SharedPreferencesUtils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 登录页面
 *
 * mvp presenter
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/21 15:05
 */
public class LoginPresenter {

    private static final String TAG = "LoginPresenter";

    private LoginActivity activity;

    private UserApi userApi;

    private Gson gson = new Gson();

    public LoginPresenter(LoginActivity activity, UserApi userApi) {
        this.activity = activity;
        this.userApi = userApi;
    }

    public void phoneLogin(final Long phone, String passwd){

        passwd = DigestUtils.md5Hex(passwd);

        final String finalPasswd = passwd;
        userApi.phoneLogin(phone,finalPasswd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SelfResult<Token>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,e.getMessage());

                        activity.loginResult("网络错误");

                    }

                    @Override
                    public void onNext(SelfResult<Token> tokenSelfResult) {

                        Log.e(TAG,tokenSelfResult.toString());

                        String message = "";

                        if (tokenSelfResult.isSuccess()){

                            Map<String, String> map = new HashMap<>();

                            String json = gson.toJson(tokenSelfResult.getData(),Token.class);

                            map.put("token",json);
                            map.put("phone",phone + "");
                            map.put("passwd", finalPasswd);
                            SharedPreferencesUtils.saveTakon(activity,map);
                            message = "1";

                        } else {
                            message = tokenSelfResult.getError();
                        }

                        activity.loginResult(message);

                    }
                });
    }

}
