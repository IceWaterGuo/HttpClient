package com.ice.httpclient.utils

import android.content.res.Resources

/**
 * Desc:扩展函数
 * Created by icewater on 2024/01/22.
 */

fun Number.px2dp(): Float {
    val f = toFloat()
    val scale: Float = Resources.getSystem().displayMetrics.density
    return (f / scale + 0.5f)
}

fun Number.dp2px(): Int {
    val f = toFloat()
    val scale: Float = Resources.getSystem().displayMetrics.density
    return (f * scale + 0.5f).toInt()
}

fun Number.dp2pxFloat(): Float {
    val f = toFloat()
    val scale: Float = Resources.getSystem().displayMetrics.density
    return f * scale
}

fun Number.sp2px(): Int {
    val f = toFloat()
    val scale: Float = Resources.getSystem().displayMetrics.scaledDensity
    return (f * scale + 0.5f).toInt()
}

fun Number.sp2pxFloat(): Float {
    val f = toFloat()
    val scale: Float = Resources.getSystem().displayMetrics.scaledDensity
    return f * scale
}