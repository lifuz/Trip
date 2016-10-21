package com.lifuz.trip.ui.presenter;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lifuz.trip.api.mine.UserApi;
import com.lifuz.trip.enums.MineState;
import com.lifuz.trip.module.common.SelfResult;
import com.lifuz.trip.module.mine.Token;
import com.lifuz.trip.ui.activity.SignUpActivity;
import com.lifuz.trip.utils.SharedPreferencesUtils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 注册页面
 *
 * mvp presenter
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/21 15:10
 */
public class SignUpPresenter {

    private static final String TAG = "SignUpPresenter";

    private SignUpActivity activity;

    private UserApi userApi;

    private Gson gson;

    public SignUpPresenter(SignUpActivity activity, UserApi userApi) {
        this.activity = activity;
        this.userApi = userApi;
        gson = new Gson();
    }

    public void phoneLogin(Long phone,String passwd){

        passwd = DigestUtils.md5Hex(passwd);

        userApi.phoneLogin(phone,passwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SelfResult<Token>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,e.getMessage());

                    }

                    @Override
                    public void onNext(SelfResult<Token> tokenSelfResult) {

                        Log.e(TAG,tokenSelfResult.toString());

                        String message = "";

                        if (tokenSelfResult.isSuccess()){

                            Map<String, String> map = new HashMap<>();

                            String json = gson.toJson(tokenSelfResult.getData(),Token.class);

                            map.put("token",json);
                            SharedPreferencesUtils.saveTakon(activity,map);
                            message = "1";

                        } else {
                            message = tokenSelfResult.getError();
                        }

                        activity.loginResult(message);

                    }
                });
    }

    public void signUp(String phone,String passwd,String userName) {
        Map<String,String> map = new HashMap<>();

        map.put("userPhone",phone);
        map.put("userName",userName);

        passwd = DigestUtils.md5Hex(passwd);

        map.put("userPasswd",passwd);

        userApi.register(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SelfResult<MineState>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e(TAG,e.getMessage(),e);

                    }

                    @Override
                    public void onNext(SelfResult<MineState> mineStateSelfResult) {
                        Log.e(TAG,mineStateSelfResult.toString());

                        activity.signUpResult(mineStateSelfResult);

                    }
                });

    }

}
