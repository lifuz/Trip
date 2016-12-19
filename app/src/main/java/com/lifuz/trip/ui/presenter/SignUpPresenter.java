package com.lifuz.trip.ui.presenter;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.lifuz.trip.api.mine.UserApi;
import com.lifuz.trip.enums.SelfState;
import com.lifuz.trip.module.common.SelfResult;
import com.lifuz.trip.module.mine.Token;
import com.lifuz.trip.ui.activity.SignUpActivity;
import com.lifuz.trip.utils.SharedPreferencesUtils;
import com.lifuz.trip.utils.security.RSAEncryptor;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.InputStream;
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

    public void phoneLogin(final Long phone, String passwd){

        passwd = new String(Hex.encodeHex(DigestUtils.md5(passwd)));

        AssetManager assetManager = activity.getAssets();

        RSAEncryptor rsaEncryptor = RSAEncryptor.getInstance();

        try {

            InputStream in = assetManager.open("rsa_public_key.pem");
            String publicKey = rsaEncryptor.getKeyFromIO(in);
            in.close();
            rsaEncryptor.loadPublicKey(publicKey);
            passwd = rsaEncryptor.encryptWithBase64(passwd,1);
            Log.e(TAG,passwd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String finalPasswd = passwd;
        userApi.phoneLogin(phone,finalPasswd,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SelfResult<Token>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,e.getMessage());
                        activity.loginResult("网络错误");

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
                .subscribe(new Observer<SelfResult<SelfState>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e(TAG,e.getMessage(),e);

                        activity.signUpResult(null);

                    }

                    @Override
                    public void onNext(SelfResult<SelfState> mineStateSelfResult) {
                        Log.e(TAG,mineStateSelfResult.toString());

                        activity.signUpResult(mineStateSelfResult);

                    }
                });

    }

}
