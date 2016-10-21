package com.lifuz.trip.ui.activity;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;

import com.lifuz.trip.R;
import com.lifuz.trip.ui.widget.PasswdEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册页面
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/21 14:19
 */
public class SignUpActivity extends BaseActivity {

    @BindView(R.id.input_name)
    EditText inputName;

    @BindView(R.id.input_password)
    PasswdEditText inputPassword;

    private boolean passwdFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);

        initView();

    }

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

    @OnClick(R.id.link_login)
    public void login(){
        finish();
    }
}
