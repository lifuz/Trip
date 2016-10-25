package com.lifuz.trip.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lifuz.trip.R;
import com.lifuz.trip.application.AppComponent;
import com.lifuz.trip.application.TripApplication;
import com.lifuz.trip.ui.component.DaggerEditUserComponent;
import com.lifuz.trip.ui.module.EditUserModule;
import com.lifuz.trip.ui.presenter.EditUserPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * 编辑用户信息页面
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/25 17:44
 */
public class EditUserActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.center_title)
    protected TextView centerTitle;

    @BindView(R.id.nav_left_iv)
    ImageView navLeft;

    @Inject
    EditUserPresenter editUserPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_user);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        centerTitle.setText("编辑资料");

        navLeft.setVisibility(View.VISIBLE);

        inject();


    }

    @OnClick(R.id.nav_left_iv
    )
    public void navLeft() {

        finish();
    }

    private void inject(){

        AppComponent appComponent = ((TripApplication) getApplication()).getAppComponent();

        DaggerEditUserComponent.builder()
                .appComponent(appComponent)
                .editUserModule(new EditUserModule(this))
                .build().inject(this);

    }
}
