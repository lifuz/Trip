package com.lifuz.trip.ui.module;

import com.lifuz.trip.api.mine.UserApi;
import com.lifuz.trip.ui.fragment.MineFragment;
import com.lifuz.trip.ui.presenter.MinePresenter;

import dagger.Module;
import dagger.Provides;

/**
 *
 * 我的页面
 *
 * mvp module
 *
 * @author: 李富
 * @email: lifuzz@163.com
 * @time: 2016/10/22 16:01
 */
@Module
public class MineModule {

    private MineFragment fragment;

    public MineModule(MineFragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    public MineFragment provideFragment(){
        return fragment;
    }

    @Provides
    public MinePresenter provideMinePresenter(UserApi userApi){

        return new MinePresenter(fragment,userApi);
    }
}
