package pers.jay.library.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.LogUtils

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
abstract class BaseApplication: Application(), Application.ActivityLifecycleCallbacks {

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
        initLibs()
        lazyInit()
    }

    private fun initLibs() {
        initLoadSir()
    }

    /**
     * 初始化loadSir，根据需求实现
     */
    open fun initLoadSir() {
//        LoadSir.beginBuilder()
//            .addCallback(ProgressCallback.Builder().setTitle("加载中").build())
//            .setDefaultCallback(ProgressCallback::class.java)
//            .commit()
    }

    abstract fun lazyInit()


    override fun onActivityPaused(activity: Activity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityStarted(activity: Activity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityDestroyed(activity: Activity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityStopped(activity: Activity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityResumed(activity: Activity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}