package com.lifuz.trip.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 *
 * fragment 的基类做一些封装
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/21 18:27
 */
public abstract class BaseFragment extends Fragment {

    protected abstract int getLayoutId();

    protected abstract void initView(View view, Bundle savedInstanceState);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(getLayoutId(),container,false);

        ButterKnife.bind(this,view);

        initView(view,savedInstanceState);

        return view;
    }


}
