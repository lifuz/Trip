package com.lifuz.trip.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.lifuz.trip.api.mine.UserApi;
import com.lifuz.trip.enums.SelfState;
import com.lifuz.trip.module.common.SelfResult;
import com.lifuz.trip.module.mine.Token;
import com.lifuz.trip.module.mine.User;
import com.lifuz.trip.ui.activity.EditUserActivity;
import com.lifuz.trip.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *编辑用户信息页面
 *
 * mvp presenter
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/25 19:00
 */
public class EditUserPresenter {


    private static final String TAG = "EditUserPresenter";

    private EditUserActivity activity;

    private UserApi userApi;

    private Gson gson = new Gson();

    public EditUserPresenter(EditUserActivity activity, UserApi userApi) {
        this.activity = activity;
        this.userApi = userApi;
    }

    public void updateUser(String sign,String occu) {

        final Token token = SharedPreferencesUtils.getToken(activity);

        if (token == null) {

            return;
        }

        final Map<String,String> map = new HashMap<>();

        map.put("signature",sign);
        map.put("occupation",occu);

        Observable.just(null)
                .flatMap(new Func1<Object, Observable<SelfState>>() {
                    @Override
                    public Observable<SelfState> call(Object o) {

                        if (token.getToken() == null) {

                            return Observable.error(new NullPointerException("token is null!!"));
                        } else if (System.currentTimeMillis() / 1000 > token.getExpire()) {
                            return Observable.error(new IllegalArgumentException("token is expires!!"));
                        } else {

                            return userApi.updateUser(token.getUserId(), token.getToken(),map);
                        }
                    }
                }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {

            @Override
            public Observable<?> call(Observable<? extends Throwable> observable) {

                return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable) {

                        if (throwable instanceof NullPointerException || throwable instanceof IllegalArgumentException) {
                            return userApi.userId(token.getUserId() + "")
                                    .doOnNext(new Action1<SelfResult<Token>>() {
                                        @Override
                                        public void call(SelfResult<Token> tokenSelfResult) {
                                            if (tokenSelfResult.isSuccess()) {

                                                Token newToken = tokenSelfResult.getData();

                                                token.setExpire(newToken.getExpire());
                                                token.setToken(newToken.getToken());

                                                Map<String, String> map = new HashMap<>();

                                                String json = gson.toJson(tokenSelfResult.getData(), Token.class);

                                                map.put("token", json);
                                                SharedPreferencesUtils.saveTakon(activity, map);

                                            }

                                        }
                                    });
                        }
                        return Observable.error(throwable);
                    }
                });

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SelfState>() {
                    @Override
                    public void call(SelfState selfState) {
                        Log.e(TAG, selfState.toString());


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, throwable.getMessage());

                    }
                });

    }

    public void getUser(final Context context) {

        final Token token = SharedPreferencesUtils.getToken(context);

        if (token == null) {

            activity.setUserInfo(new SelfResult<User>(false, "1"));

            return;
        }

        Observable.just(null)
                .flatMap(new Func1<Object, Observable<SelfResult<User>>>() {
                    @Override
                    public Observable<SelfResult<User>> call(Object o) {

                        if (token.getToken() == null) {

                            return Observable.error(new NullPointerException("token is null!!"));
                        } else if (System.currentTimeMillis() / 1000 > token.getExpire()) {
                            return Observable.error(new IllegalArgumentException("token is expires!!"));
                        } else {

                            return userApi.getUser(token.getUserId(), token.getToken());
                        }
                    }
                }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {

            @Override
            public Observable<?> call(Observable<? extends Throwable> observable) {

                return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable) {

                        if (throwable instanceof NullPointerException || throwable instanceof IllegalArgumentException) {
                            return userApi.userId(token.getUserId() + "")
                                    .doOnNext(new Action1<SelfResult<Token>>() {
                                        @Override
                                        public void call(SelfResult<Token> tokenSelfResult) {
                                            if (tokenSelfResult.isSuccess()) {

                                                Token newToken = tokenSelfResult.getData();

                                                token.setExpire(newToken.getExpire());
                                                token.setToken(newToken.getToken());

                                                Map<String, String> map = new HashMap<>();

                                                String json = gson.toJson(tokenSelfResult.getData(), Token.class);

                                                map.put("token", json);
                                                SharedPreferencesUtils.saveTakon(context, map);

                                            }

                                        }
                                    });
                        }
                        return Observable.error(throwable);
                    }
                });

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SelfResult<User>>() {
                    @Override
                    public void call(SelfResult<User> userSelfResult) {
                        Log.e(TAG, userSelfResult.toString());

                        activity.setUserInfo(userSelfResult);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, throwable.getMessage());


                        activity.setUserInfo(new SelfResult<User>(false, throwable.getMessage()));
                    }
                });

    }



}
