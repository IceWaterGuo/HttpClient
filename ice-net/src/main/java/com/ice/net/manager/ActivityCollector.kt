package com.ice.net.manager

import android.app.Activity
import java.util.*

/**
 * Desc:管理所有的活动（Activity）
 * Created by icewater on 2020-04-03.
 */
object ActivityCollector {
    var activities: MutableList<Activity> = ArrayList()

    /**
     * 添加Activity
     * @param activity 添加的Activity对象
     */
    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    /**
     * 删除Activity
     * @param activity 删除的Activity对象
     */
    fun removeActivity(activity: Activity?) {
        activities.remove(activity)
    }

    /**
     * 关闭指定的Activity
     * @param activityName 需要关闭的Activity包名类名
     */
    fun finishOneActivity(activityName: String) {
        //在activities集合中找到类名与指定类名相同的Activity就关闭
        for (activity in activities) {
            val name = activity.javaClass.name //activity的包名+类名
            if (name == activityName) {
                if (activity.isFinishing) {
                    //当前activity如果已经Finish，则只从activities清除就好了
                    activities.remove(activity)
                } else {
                    //没有Finish则Finish
                    activity.finish()
                }
            }
        }
    }

    /**
     * 只保留某个Activity，关闭其他所有Activity
     * @param activityName 要保留的Activity类名
     */
    fun finishOtherActivity(activityName: String) {
        for (activity in activities) {
            val name = activity.javaClass.name //activity的类名
            if (name != activityName) {
                if (activity.isFinishing) {
                    //当前activity如果已经Finish，则只从activities清除就好了
                    activities.remove(activity)
                } else {
                    //没有Finish则Finish
                    activity.finish()
                }
            }
        }
    }

    /**
     * 关闭所有Activity
     */
    fun finishAll() {
        for (activity in activities) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
        activities.clear()
    }

}