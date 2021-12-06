package com.ice.net.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast

/**
 * Desc:吐司
 * Created by icewater on 2021/03/08.
 */
object ToastUtils {
    private var mToast: Toast? = null
    @SuppressLint("ShowToast")
    fun showToast(context: Context, msg: String) {
        if (mToast == null) {
            mToast = Toast.makeText(context.applicationContext, "", Toast.LENGTH_SHORT)
        }
        mToast!!.setText(msg)
        mToast!!.show()
    }
}