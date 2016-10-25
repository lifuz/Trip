package com.lifuz.trip.ui.module;

import com.lifuz.trip.api.mine.UserApi;
import com.lifuz.trip.ui.activity.EditUserActivity;
import com.lifuz.trip.ui.presenter.EditUserPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 编辑用户信息页面
 *
 * mvp module
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/25 19:03
 */
@Module
public class EditUserModule {

    private EditUserActivity activity;

    public EditUserModule(EditUserActivity activity) {
        this.activity = activity;
    }

    @Provides
    public EditUserActivity provideActivity(){
        return activity;
    }

    @Provides
    public EditUserPresenter provideEditUserPresenter(UserApi userApi){
        return new EditUserPresenter(activity,userApi);
    }
}
