package com.ice.net.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Desc:网络状态工具类
 * Created by icewater on 2021/03/08.
 */
public class HttpUtils {
    /**
     * 判断网络状态是否可用
     * @param context
     * @return
     */
    public static boolean isNetWorkAvailable(Context context){
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
