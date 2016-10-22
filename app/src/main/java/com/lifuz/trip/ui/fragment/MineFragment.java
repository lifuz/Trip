package com.lifuz.trip.ui.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lifuz.trip.R;
import com.lifuz.trip.application.AppComponent;
import com.lifuz.trip.application.TripApplication;
import com.lifuz.trip.module.common.SelfResult;
import com.lifuz.trip.module.mine.User;
import com.lifuz.trip.ui.activity.LoginActivity;
import com.lifuz.trip.ui.component.DaggerMineComponent;
import com.lifuz.trip.ui.module.MineModule;
import com.lifuz.trip.ui.presenter.MinePresenter;
import com.lifuz.trip.utils.SharedPreferencesUtils;
import com.lifuz.trip.utils.SnackBarUtils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的模块的fragment
 * @author: 李富
 * @email: lifuzz@163.com
 * @time: 2016/7/12 21:34
 */
public class MineFragment extends BaseFragment {

    private static final String TAG = "MineFragment";

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.center_title)
    protected TextView centerTitle;

    @Inject
    MinePresenter minePresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        centerTitle.setText("李富");

        toolbar.setElevation(0F);

        inject();

        minePresenter.getUser(getContext());

    }

    public void user(SelfResult<User> selfResult) {

        if (selfResult.isSuccess()) {

            Log.e(TAG,"处理成功数据");

            User user = selfResult.getData();

            centerTitle.setText(user.getUserName());


        } else {
            if ("1".equals(selfResult.getError())) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            } else {
                SnackBarUtils.makeLong(toolbar,selfResult.getError());
            }
        }
    }

    private void inject() {

        AppComponent appComponent = ((TripApplication) getActivity().getApplication()).getAppComponent();

        DaggerMineComponent.builder().appComponent(appComponent)
                .mineModule(new MineModule(this)).build().inject(this);

    }

    @OnClick(R.id.mine_setting)
    public void setting(){


        Map<String, String> map = new HashMap<>();

        map.put("token","");
        SharedPreferencesUtils.saveTakon(getContext(),map);

        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();

    }


}
