package com.ice.httpclient.utils;

import android.util.Log;

/**
 * Desc:Log打印工具类
 * Created by icewater on 2021/12/29.
 */
public class AppLog {
    public static final String APP_TAG = "UtilLog";

    public static boolean DEBUG = true;


    public static void e(String msg) {
        if (DEBUG) {
            Log.e(APP_TAG, msg);
        }
    }

    public static void e(Object obj, String msg) {
        if (DEBUG) {
            Log.e(obj.getClass().toString(), ", " + msg);
        }
    }

    public static void setDEBUG(boolean DEBUG) {
        AppLog.DEBUG = DEBUG;
    }
}
