package com.jinn.projecty.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.Bundle

object ActivityManager {
    private var alivedActivityCount = 0

    private var TAG = "ActivityManager"

    private val tasks = mutableListOf<Activity>()


    private fun push(activity: Activity) {
        tasks.add(activity)
    }

    private fun pop(activity: Activity) {
        tasks.remove(activity)
    }

    fun top(): Activity {
        return tasks.last()
    }

    /**
     * 关闭所有activity
     */
    fun finishAllActivity(callback: (() -> Unit)? = null) {
        val it = tasks.iterator()
        while (it.hasNext()) {
            val item = it.next()
            it.remove()
            item.finish()
        }
        callback?.invoke()
    }

    /**
     * 关闭其它activity
     */
    fun finishOtherActivity(clazz: Class<out Activity>) {
        val it = tasks.iterator()
        while (it.hasNext()) {
            val item = it.next()
            if (item::class.java != clazz) {
                it.remove()
                item.finish()
            }
        }
    }


    /**
     * 关闭activity
     */
    fun finishActivity(clazz: Class<out Activity>) {
        val it = tasks.iterator()
        while (it.hasNext()) {
            val item = it.next()
            if (item::class.java == clazz) {
                it.remove()
                item.finish()
                break
            }
        }
    }


    /**
     * activity是否在栈中
     */
    fun isActivityExists(clazz: Class<out Activity>): Boolean {
        for (task in tasks) {
            if (task::class.java == clazz) {
                return true
            }
        }
        return false
    }


    /**
     * Activity是否销毁
     * @param context
     */
    fun isActivityDestroy(context: Context): Boolean {
        val activity = findActivity(context)
        return if (activity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                activity.isDestroyed || activity.isFinishing
            } else activity.isFinishing
        } else true
    }

    /**
     * ContextWrapper是context的包装类，AppcompatActivity，service，application实际上都是ContextWrapper的子类
     * AppcompatXXX类的context都会被包装成TintContextWrapper
     * @param context
     */
    private fun findActivity(context: Context): Activity? {
        // 怎么判断context是不是Activity
        if (context is Activity) { // 这种方法不够严谨
            return context
        } else if (context is ContextWrapper) {
            return findActivity(context.baseContext)
        }
        return null
    }


    fun register(application: Application, listener: AppStateListener) {
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                LogUtils.d(TAG, "onActivityCreated:${p0.componentName},totalCount:$alivedActivityCount")
                push(p0)
            }

            override fun onActivityStarted(p0: Activity) {
                alivedActivityCount++
                LogUtils.d(TAG, "onActivityStarted:${p0.componentName},totalCount:$alivedActivityCount")
                if (alivedActivityCount == 1) {
                    listener.onFront(p0)
                }
            }

            override fun onActivityResumed(p0: Activity) {
                LogUtils.d(TAG, "onActivityResumed:${p0.componentName},totalCount:$alivedActivityCount")
            }

            override fun onActivityPaused(p0: Activity) {
                LogUtils.d(TAG, "onActivityPaused:${p0.componentName},totalCount:$alivedActivityCount")
            }

            override fun onActivityStopped(p0: Activity) {
                alivedActivityCount--
                LogUtils.d(TAG, "onActivityStopped:${p0.componentName},totalCount:$alivedActivityCount")
                if (alivedActivityCount == 0) {
                    listener.onBack(p0)
                }
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
                LogUtils.d(
                    TAG,
                    "onActivitySaveInstanceState:${p0.componentName},totalCount:$alivedActivityCount"
                )
            }

            override fun onActivityDestroyed(p0: Activity) {
                LogUtils.d(TAG, "onActivityDestroyed:${p0.componentName},totalCount:$alivedActivityCount")
                pop(p0)
            }

        })
    }

    fun unregister(application: Application) {
        //application.unregisterActivityLifecycleCallbacks()
    }

}

interface AppStateListener {

    /**
     * 前台
     */
    fun onFront(activity: Activity)

    /**
     * 后台
     */
    fun onBack(activity: Activity)
}