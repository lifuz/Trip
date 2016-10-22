package com.lifuz.trip.ui.component;

import com.lifuz.trip.application.AppComponent;
import com.lifuz.trip.ui.ActivityScope;
import com.lifuz.trip.ui.fragment.MineFragment;
import com.lifuz.trip.ui.module.MineModule;

import dagger.Component;

/**
 *
 * 我的页面
 *
 * mvp component
 *
 * @author: 李富
 * @email: lifuzz@163.com
 * @time: 2016/10/22 16:05
 */
@ActivityScope
@Component(modules = MineModule.class, dependencies = AppComponent.class)
public interface MineComponent {

    void inject(MineFragment mineFragment);
}
