package com.ice.net.base.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hjq.toast.ToastUtils;

/**
 * Desc:fragment基类
 * Created by icewater on 2021/03/08.
 */
public abstract class BaseFragment extends Fragment {
    public int REQUEST_CODE = Math.abs(this.getClass().getSimpleName().hashCode() % 60000);

    public Activity mActivity;
    protected View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        mView = inflater.inflate(setLayoutResID(), container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView();
        fillData();
        initToolbar();
        initView();
        initListeners();
        initData();
    }

    public void bindView() {

    }

    public void initToolbar() {

    }

    /**
     * 设置布局文件
     *
     * @return 布局文件ID
     */
    public abstract int setLayoutResID();

    public void fillData() {

    }

    /**
     * 初始化view,必须实现
     */
    public abstract void initView();

    /**
     * 初始化Listener,需要实现是覆盖
     */
    public void initListeners() {

    }

    /**
     * 初始化数据
     */
    public void initData() {

    }

    /**
     * 打印吐司
     *
     * @param msg
     */
    public void showToast(String msg) {
        ToastUtils.show(msg);
    }

    /**
     * 显示加载进度条
     *
     * @param title
     */
    public abstract void showLoadingDialog(String title);

    public abstract void hideLoadingDialog();
}
