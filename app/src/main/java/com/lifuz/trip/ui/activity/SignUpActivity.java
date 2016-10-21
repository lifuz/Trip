package com.lifuz.trip.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.lifuz.trip.R;
import com.lifuz.trip.application.AppComponent;
import com.lifuz.trip.application.TripApplication;
import com.lifuz.trip.enums.MineState;
import com.lifuz.trip.module.common.SelfResult;
import com.lifuz.trip.ui.component.DaggerSignUpComponent;
import com.lifuz.trip.ui.module.SignUpModule;
import com.lifuz.trip.ui.presenter.SignUpPresenter;
import com.lifuz.trip.ui.widget.CustomDialog;
import com.lifuz.trip.ui.widget.PasswdEditText;
import com.lifuz.trip.utils.SnackBarUtils;
import com.lifuz.trip.utils.StringUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册页面
 * <p>
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/21 14:19
 */
public class SignUpActivity extends BaseActivity {

    @BindView(R.id.input_name)
    EditText inputName;

    @BindView(R.id.input_password)
    PasswdEditText inputPassword;

    @BindView(R.id.btn_signup)
    AppCompatButton btnSignUp;

    private boolean passwdFlag = true;

    private String phone;
    private String passwd;

    private CustomDialog dialog;

    @Inject
    SignUpPresenter signUpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);

        initView();

        inject();

        Intent it = getIntent();

        phone = it.getStringExtra("phone");

    }

    /**
     * 注册结果处理
     * @param mineStateSelfResult 注册结果信息
     */
    public void signUpResult(SelfResult<MineState> mineStateSelfResult) {

        if (mineStateSelfResult == null) {
            dialog.dismiss();
            btnSignUp.setEnabled(true);
            SnackBarUtils.makeShort(btnSignUp, mineStateSelfResult.getError()).danger();
            return;
        }

        if (mineStateSelfResult.isSuccess()) {
            dialog.setMessage("正在登录...");
            signUpPresenter.phoneLogin(Long.parseLong(phone), passwd);

        } else {
            dialog.dismiss();
            btnSignUp.setEnabled(true);
            SnackBarUtils.makeShort(btnSignUp, mineStateSelfResult.getError()).danger();
        }


    }

    /**
     * 处理登录结果
     * @param message 结果信息
     */
    public void loginResult(String message) {

        dialog.dismiss();

        if (message.equals("1")) {

            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();

        } else {

            SnackBarUtils.makeShort(btnSignUp, "注册成功");
            login();
        }

    }

    /**
     * 注册按钮点击事件处理
     */
    @OnClick(R.id.btn_signup)
    public void signUp() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(btnSignUp.getWindowToken(), 0);

        btnSignUp.setEnabled(false);

        phone = phone.trim();

        String userName = inputName.getText().toString();

        if (StringUtils.isEmpty(userName)) {
            SnackBarUtils.makeShort(btnSignUp, "用户名不能为空").danger();
            btnSignUp.setEnabled(true);
            return;
        }

        passwd = inputPassword.getText().toString();

        if (StringUtils.isEmpty(passwd)) {
            SnackBarUtils.makeShort(btnSignUp, "密码不能为空").danger();
            btnSignUp.setEnabled(true);
            return;
        }

        if (passwd.length() < 6 || passwd.length() > 20) {
            SnackBarUtils.makeShort(btnSignUp, "密码长度为6-20个字符").danger();
            btnSignUp.setEnabled(true);
            return;
        }

        dialog = new CustomDialog(this, R.style.CustomDialog);
        dialog.setMessage("正在注册...");
        dialog.show();

        signUpPresenter.signUp(phone, passwd, userName);


    }

    /**
     * 前往登录按钮点击事件处理
     */
    @OnClick(R.id.link_login)
    public void login() {

        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }

    /**
     * 注入
     */
    private void inject() {

        AppComponent appComponent = ((TripApplication) getApplication()).getAppComponent();

        DaggerSignUpComponent.builder().appComponent(appComponent)
                .signUpModule(new SignUpModule(this))
                .build().inject(this);

    }

    /**
     * 初始化组件
     */
    private void initView() {


        inputPassword.setOnDrawableRightListener(new PasswdEditText.OnDrawableRightListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDrawableRightClick(View view) {
                Drawable rightDrawable = null;
                inputPassword.setSelected(false);
                if (passwdFlag) {

                    rightDrawable = getDrawable(R.mipmap.ic_action_visibility);

                    //设置明文密码
                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());


                    passwdFlag = false;

                } else {


                    rightDrawable = getDrawable(R.mipmap.ic_action_visibility_off);

                    //设置密文密码
                    inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    passwdFlag = true;

                }

                inputPassword.postInvalidate();
                //切换后将EditText光标置于末尾
                CharSequence charSequence = inputPassword.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());

                }

                inputPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);


            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.dismiss();

    }
}
