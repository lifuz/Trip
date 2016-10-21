package com.lifuz.trip.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lifuz.trip.R;
import com.lifuz.trip.ui.widget.PasswdEditText;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 * 登录页面
 * <p>
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/20 16:57
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";


    @BindView(R.id.login_phone_et)
    protected TextInputEditText login_phone_et;

    @BindView(R.id.login_passwd_et)
    protected PasswdEditText login_passwd_et;

    @BindView(R.id.link_signup)
    protected TextView linkSignUp;


    private boolean passwdFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        initView();

    }

    private void initView() {


        linkSignUp.setFocusable(true);
        linkSignUp.requestFocus();

        linkSignUp.setFocusableInTouchMode(true);
        linkSignUp.requestFocusFromTouch();

        login_passwd_et.setOnDrawableRightListener(new PasswdEditText.OnDrawableRightListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDrawableRightClick(View view) {
                Drawable rightDrawable = null;
                login_passwd_et.setSelected(false);
                if (passwdFlag) {

                    rightDrawable = getDrawable(R.mipmap.ic_action_visibility);

                    //设置明文密码
                    login_passwd_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());


                    passwdFlag = false;

                } else {


                    rightDrawable = getDrawable(R.mipmap.ic_action_visibility_off);

                    //设置密文密码
                    login_passwd_et.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    passwdFlag = true;

                }

                login_passwd_et.postInvalidate();
                //切换后将EditText光标置于末尾
                CharSequence charSequence = login_passwd_et.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());

                }

                login_passwd_et.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);


            }
        });

    }

    @OnClick(R.id.link_signup)
    public void signUP() {

        //打开注册页面
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");

                    // 提交用户信息（此方法可以不调用）
                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                }
            }
        });
        registerPage.show(this);

    }

}
