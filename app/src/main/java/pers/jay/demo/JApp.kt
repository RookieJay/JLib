package pers.jay.demo

import android.content.Context
import com.kingja.loadsir.core.LoadSir
import pers.jay.demo.loadsir.EmptyCallback
import pers.jay.demo.loadsir.ErrorCallback
import pers.jay.demo.loadsir.LoadingCallback
import pers.jay.demo.loadsir.RetryCallback
import pers.jay.library.app.BaseApplication
import pers.jay.library.network.IAppInfo

class JApp: BaseApplication(), IAppInfo {

    override fun lazyInit() {

    }

    override fun getAppVersion() {

    }

    override fun getAppContext(): Context {
        return this
    }

    override fun initLoadSir() {
        super.initLoadSir()
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())
            .addCallback(EmptyCallback())
            .addCallback(ErrorCallback())
            .addCallback(RetryCallback())
            .setDefaultCallback(LoadingCallback::class.java)
            .commit()
    }
}