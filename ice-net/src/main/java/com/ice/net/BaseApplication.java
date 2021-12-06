package com.ice.net;

import android.app.Application;
import android.content.Context;

import com.hjq.toast.ToastUtils;

/**
 * Desc:application基类
 * Created by icewater on 2021/03/12.
 */
public class BaseApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Toast
        ToastUtils.init(this);
        mContext = getApplicationContext();
    }

    /**
     * 获取application context
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }
}
