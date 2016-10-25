package com.lifuz.trip.ui.presenter;

import com.lifuz.trip.api.mine.UserApi;
import com.lifuz.trip.ui.activity.EditUserActivity;

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

    private EditUserActivity activity;

    private UserApi userApi;

    public EditUserPresenter(EditUserActivity activity, UserApi userApi) {
        this.activity = activity;
        this.userApi = userApi;
    }



}
