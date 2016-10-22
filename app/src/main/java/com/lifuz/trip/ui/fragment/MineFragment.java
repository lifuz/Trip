package com.lifuz.trip.ui.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.lifuz.trip.R;
import com.lifuz.trip.ui.activity.LoginActivity;
import com.lifuz.trip.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 财富模块的fragment
 * @author: 李富
 * @email: lifuzz@163.com
 * @time: 2016/7/12 21:34
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.center_title)
    protected TextView centerTitle;

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
