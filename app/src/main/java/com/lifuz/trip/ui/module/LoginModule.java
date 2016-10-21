package com.lifuz.trip.ui.module;

import com.lifuz.trip.api.mine.UserApi;
import com.lifuz.trip.ui.activity.LoginActivity;
import com.lifuz.trip.ui.presenter.LoginPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 登录页面
 *
 * mvp module
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/21 15:03
 */
@Module
public class LoginModule {

    private LoginActivity activity;

    public LoginModule(LoginActivity activity) {
        this.activity = activity;
    }

    @Provides
    public LoginActivity provideActivity(){
        return activity;
    }


    @Provides
    public LoginPresenter provideLoginPresenter(UserApi userApi){

        return new LoginPresenter(activity,userApi);

    }

}
