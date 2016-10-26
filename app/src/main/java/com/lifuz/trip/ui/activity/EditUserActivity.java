package com.lifuz.trip.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lifuz.trip.R;
import com.lifuz.trip.application.AppComponent;
import com.lifuz.trip.application.TripApplication;
import com.lifuz.trip.module.common.SelfResult;
import com.lifuz.trip.module.mine.User;
import com.lifuz.trip.ui.component.DaggerEditUserComponent;
import com.lifuz.trip.ui.module.EditUserModule;
import com.lifuz.trip.ui.presenter.EditUserPresenter;
import com.lifuz.trip.utils.SnackBarUtils;
import com.lifuz.trip.utils.StringUtils;

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

    private static final String TAG = "EditUserActivity";

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.center_title)
    protected TextView centerTitle;

    @BindView(R.id.nav_left_iv)
    ImageView navLeft;

    @BindView(R.id.edit_sign)
    EditText editSign;

    @BindView(R.id.edit_occu)
    EditText editOccu;

    @Inject
    EditUserPresenter editUserPresenter;

    private User user;

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

        editUserPresenter.getUser(this);


    }

    /**
     * 设置获取的用户信息
     * @param selfResult
     */
    public void setUserInfo(SelfResult<User> selfResult){

        if (selfResult.isSuccess()) {

            Log.e(TAG, "处理成功数据");

           user = selfResult.getData();

            if (!StringUtils.isEmpty(user.getSignature())){
                editSign.setText(user.getSignature());
            }

            if (!StringUtils.isEmpty(user.getOccupation())) {
                editOccu.setText(user.getOccupation());
            }


        } else {
            if ("1".equals(selfResult.getError())) {
                startActivity(new Intent(this, LoginActivity.class));
                this.finish();
            } else {
                SnackBarUtils.makeLong(toolbar, selfResult.getError()).danger();
            }
        }

    }

    /**
     * 返回键点击事件
     */
    @OnClick(R.id.nav_left_iv)
    public void navLeft() {

        String sign = editSign.getText().toString();

        String occu = editOccu.getText().toString();

        sign.trim();
        occu.trim();

        editUserPresenter.updateUser(sign,occu);

        Intent it = new Intent(this,MainActivity.class);

        it.putExtra("page",3);

        startActivity(it);

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
