package pers.jay.demo.loadsir

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.kingja.loadsir.callback.Callback
import pers.jay.library.base.StateListener
import pers.jay.library.base.livedata.StateLiveData

class LoadSirLiveData<T>: StateLiveData<T>() {

    fun observeState(
        statusView: View? = null,
        statusCallback: StatusCallback?= null,
        owner: LifecycleOwner,
        listenerBuilder: StateListener<T>.() -> Unit
    ) {
        val listener = StateListener<T>().apply(listenerBuilder)
        val observer = object : LoadSirLiveDataObserver<T>(statusView) {

            override fun getEmptyCallback(): Class<out Callback>? {
                return statusView?.let {
                    if (statusCallback == null) {
                        throw IllegalArgumentException("emptyCallback can not be null when setting statusView")
                    }
                    statusCallback.emptyCallback
                }
            }

            override fun getErrorCallback(): Class<out Callback>? {
                return statusView?.let {
                    if (statusCallback == null) {
                        throw IllegalArgumentException("errorCallback can not be null when setting statusView")
                    }
                    statusCallback.errorCallback
                }
            }

            override fun getLoadingCallback(): Class<out Callback>? {
                return statusView?.let {
                    if (statusCallback == null) {
                        throw IllegalArgumentException("loadingCallback can not be null when setting statusView")
                    }
                    statusCallback.loadingCallback
                }
            }

            override fun onSuccess(data: T) {
                listener.successAction?.invoke(data)
            }

            override fun onDataEmpty() {
                listener.emptyAction?.invoke()
            }

            override fun onError(throwable: Throwable, msg: String) {
                listener.errorAction?.invoke(throwable, msg)
            }


            override fun onCompletion() {
                listener.completeAction?.invoke()
            }

            override fun onReload(v: View?) {

            }
        }
        super.observe(owner, observer)
    }
}