package com.lifuz.trip.ui.component;

import com.lifuz.trip.application.AppComponent;
import com.lifuz.trip.ui.ActivityScope;
import com.lifuz.trip.ui.activity.SignUpActivity;
import com.lifuz.trip.ui.module.SignUpModule;

import dagger.Component;

/**
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/21 15:15
 */
@ActivityScope
@Component(modules = SignUpModule.class, dependencies = AppComponent.class)
public interface SignUpComponent {

    void inject(SignUpActivity activity);

}
