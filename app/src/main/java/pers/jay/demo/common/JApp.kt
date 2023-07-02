package pers.jay.demo.common

import android.content.Context
import com.kingja.loadsir.core.LoadSir
import pers.jay.demo.BuildConfig
import pers.jay.demo.loadsir.EmptyCallback
import pers.jay.demo.loadsir.ErrorCallback
import pers.jay.demo.loadsir.LoadingCallback
import pers.jay.demo.loadsir.RetryCallback
import pers.jay.library.app.BaseApplication

class JApp: BaseApplication() {

    val instance1 : JApp by lazy {
        this
    }

    companion object {
        lateinit var instance: JApp

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun lazyInit() {

    }

    override fun getAppContext(): Context {
        return this
    }

    override fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }

    private fun initLoadSir() {
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())
            .addCallback(EmptyCallback())
            .addCallback(ErrorCallback())
            .addCallback(RetryCallback())
            .setDefaultCallback(LoadingCallback::class.java)
            .commit()
    }

}