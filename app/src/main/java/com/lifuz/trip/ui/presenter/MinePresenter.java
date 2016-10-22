package com.lifuz.trip.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.lifuz.trip.api.mine.UserApi;
import com.lifuz.trip.module.common.SelfResult;
import com.lifuz.trip.module.mine.Token;
import com.lifuz.trip.module.mine.User;
import com.lifuz.trip.ui.fragment.MineFragment;
import com.lifuz.trip.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 我的页面
 * <p>
 * mvp presenter
 *
 * @author: 李富
 * @email: lifuzz@163.com
 * @time: 2016/10/22 15:59
 */

public class MinePresenter {

    private static final String TAG = "MinePresenter";

    private MineFragment fragment;

    private UserApi userApi;

    private Gson gson = new Gson();

    public MinePresenter(MineFragment fragment, UserApi userApi) {
        this.fragment = fragment;
        this.userApi = userApi;
    }

    public void getUser(final Context context) {

        final Token token = SharedPreferencesUtils.getToken(context);

        if (token == null) {

            fragment.user(new SelfResult<User>(false,"1"));

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

                                                String json = gson.toJson(tokenSelfResult.getData(),Token.class);

                                                map.put("token",json);
                                                SharedPreferencesUtils.saveTakon(context,map);

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
                        Log.e(TAG,userSelfResult.toString());

                        fragment.user(userSelfResult);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG,throwable.getMessage());

                        fragment.user(new SelfResult<User>(false,throwable.getMessage()));
                    }
                });

    }


}
