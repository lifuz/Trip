package com.lifuz.trip.ui.presenter;

import com.lifuz.trip.api.mine.UserApi;
import com.lifuz.trip.ui.activity.SignUpActivity;

/**
 * 注册页面
 *
 * mvp presenter
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/21 15:10
 */
public class SignUpPresenter {

    private static final String TAG = "SignUpPresenter";

    private SignUpActivity activity;

    private UserApi userApi;

    public SignUpPresenter(SignUpActivity activity, UserApi userApi) {
        this.activity = activity;
        this.userApi = userApi;
    }
}
