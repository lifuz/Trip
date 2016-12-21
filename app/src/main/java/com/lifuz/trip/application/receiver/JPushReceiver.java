package com.lifuz.trip.application.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.lifuz.trip.api.mine.UserApi;
import com.lifuz.trip.application.AppComponent;
import com.lifuz.trip.application.TripApplication;
import com.lifuz.trip.enums.SelfState;
import com.lifuz.trip.module.common.SelfResult;
import com.lifuz.trip.module.mine.Token;
import com.lifuz.trip.ui.activity.MainActivity;
import com.lifuz.trip.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

    private Gson gson = new Gson();

	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[JPushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[JPushReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

            Map<String,String> map = new HashMap<>();
            map.put("jpushId",regId);
            SharedPreferencesUtils.saveTakon(context,map);

            final Token token = SharedPreferencesUtils.getToken(context);

            if (token != null) {

                AppComponent appComponent = TripApplication.getTrip().getAppComponent();

                UserApi userApi = appComponent.getUserApi();

                updateJpushId(token,regId,userApi);


            }
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[JPushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	processCustomMessage(context, bundle);
        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[JPushReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[JPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        	
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[JPushReceiver] 用户点击打开了通知");

            for (String key : bundle.keySet()) {
                if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                    try {
                        JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                        Iterator<String> it =  json.keys();

                        while (it.hasNext()) {
                            String myKey = it.next().toString();
                            if (myKey.equals("type")){
                                String value = json.optString(myKey);

                                if (value.equals("no_sign")){
                                    Intent sign = new Intent(context,MainActivity.class);

                                    sign.putExtra("page",3);
                                    EventBus.getDefault().post("no_sign","finish");
                                    context.startActivity(sign);

                                }

                            }
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Get message extra JSON error!");
                    }
                }
            }
            
        	//打开自定义的Activity
//        	Intent i = new Intent(context, TestActivity.class);
//        	i.putExtras(bundle);
//        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//        	context.startActivity(i);
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[JPushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[JPushReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[JPushReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

    private void updateJpushId(final Token token,String jpushId,final UserApi userApi){
        final Map<String,String> map = new HashMap<>();

        map.put("jpushId",jpushId);

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
                                                SharedPreferencesUtils.saveTakon(TripApplication.getContext(), map);

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
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
	}
}
