package com.lifuz.trip.ui.component;

import com.lifuz.trip.application.AppComponent;
import com.lifuz.trip.ui.ActivityScope;
import com.lifuz.trip.ui.activity.LoginActivity;
import com.lifuz.trip.ui.module.LoginModule;

import dagger.Component;

/**
 * 登录页面
 *
 * mvp component
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/21 15:02
 */
@ActivityScope
@Component(modules = LoginModule.class ,dependencies = AppComponent.class)
public interface LoginComponent {

    void inject(LoginActivity activity);

}
