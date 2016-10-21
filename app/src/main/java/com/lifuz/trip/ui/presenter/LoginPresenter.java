package com.lifuz.trip.ui.presenter;

import com.lifuz.trip.api.mine.UserApi;
import com.lifuz.trip.ui.activity.LoginActivity;

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

    public LoginPresenter(LoginActivity activity, UserApi userApi) {
        this.activity = activity;
        this.userApi = userApi;
    }

}
