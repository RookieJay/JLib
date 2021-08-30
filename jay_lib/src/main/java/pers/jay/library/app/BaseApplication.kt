package pers.jay.library.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.LogUtils
import pers.jay.library.network.IAppInfo

/**
 * 静态内部类式单例kotlin实现
 * class SingletonDemo private constructor() {
        companion object {
        val instance = SingletonHolder.holder
        }
        private object SingletonHolder {
        val holder= SingletonDemo()
        }
    }
 */
abstract class BaseApplication: Application(), IAppInfo, Application.ActivityLifecycleCallbacks {

    companion object {
        private lateinit var instance : BaseApplication
        fun instance() = instance
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        LogUtils.d("onCreate-" + this.javaClass.simpleName)
        lazyInit()
    }

    abstract fun lazyInit()

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityResumed(activity: Activity) {
    }


}