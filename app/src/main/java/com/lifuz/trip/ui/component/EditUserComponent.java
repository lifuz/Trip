package com.lifuz.trip.ui.component;

import com.lifuz.trip.application.AppComponent;
import com.lifuz.trip.ui.ActivityScope;
import com.lifuz.trip.ui.activity.EditUserActivity;
import com.lifuz.trip.ui.module.EditUserModule;

import dagger.Component;

/**
 * 用户信息编辑页面
 *
 * mvp component
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/25 19:06
 */
@ActivityScope
@Component(modules = EditUserModule.class ,dependencies = AppComponent.class)
public interface EditUserComponent {

    void inject(EditUserActivity activity);
}
