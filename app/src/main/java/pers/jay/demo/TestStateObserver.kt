package pers.jay.demo

import android.view.View
import com.kingja.loadsir.callback.Callback
import pers.jay.demo.loadsir.EmptyCallback
import pers.jay.demo.loadsir.ErrorCallback
import pers.jay.demo.loadsir.LoadingCallback
import pers.jay.demo.loadsir.RetryCallback
import pers.jay.library.base.livedata.BaseStateLiveDataObserver

open class TestStateObserver<T>(view: View?): BaseStateLiveDataObserver<T>(view) {

    override fun getErrorCallback(): Class<out Callback> {
        return RetryCallback::class.java
    }

    override fun getEmptyCallback(): Class<out Callback> {
        return EmptyCallback::class.java
    }

    override fun getLoadingCallback(): Class<out Callback> {
        return LoadingCallback::class.java
    }
}