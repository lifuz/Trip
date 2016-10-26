package com.lifuz.trip.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lifuz.trip.R;
import com.lifuz.trip.ui.activity.BaseActivity;
import com.lifuz.trip.ui.activity.LoginActivity;
import com.lifuz.trip.utils.SharedPreferencesUtils;

import org.simple.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置页面
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/26 17:22
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.center_title)
    protected TextView centerTitle;

    @BindView(R.id.nav_left_iv)
    ImageView navLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        centerTitle.setText("设置");

        navLeft.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.setting_logout)
    public void logout() {
        Map<String, String> map = new HashMap<>();

        map.put("token", "");
        SharedPreferencesUtils.saveTakon(this, map);

        startActivity(new Intent(this, LoginActivity.class));

        EventBus.getDefault().post("lifuz","finish");

    }

    @OnClick(R.id.nav_left_iv)
    public void navLeft(){

        finish();
    }

}
