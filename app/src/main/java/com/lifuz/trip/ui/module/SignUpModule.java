package com.lifuz.trip.ui.module;

import com.lifuz.trip.api.mine.UserApi;
import com.lifuz.trip.ui.activity.SignUpActivity;
import com.lifuz.trip.ui.presenter.SignUpPresenter;

import dagger.Module;
import dagger.Provides;

/**
 *
 * 注册页面
 *
 * mvp module
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/21 15:11
 */
@Module
public class SignUpModule {

    private SignUpActivity activity;

    public SignUpModule(SignUpActivity activity) {
        this.activity = activity;
    }

    @Provides
    public SignUpActivity provideActivity() {
        return activity;
    }

    @Provides
    public SignUpPresenter provideSignUpPresenter(UserApi userApi) {
        return new SignUpPresenter(activity,userApi);
    }

}
