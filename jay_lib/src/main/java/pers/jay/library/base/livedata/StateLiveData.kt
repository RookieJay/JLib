package pers.jay.library.base.livedata

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.kingja.loadsir.callback.Callback
import pers.jay.library.loadsir.ViewStatusCallback
import pers.jay.library.network.BaseResponse

/**
 * @Author RookieJay
 * @Time 2021/5/31 10:39
 * @Description 含有数据状态的LiveData。
 * 封装了[BaseResponse], 以及封装[observeState]方法，使用Kotlin DSL 消除回调，使代码更具统一性。
 */
open class StateLiveData<T> : SingleLiveData<BaseResponse<T>>() {

    fun observeState(owner: LifecycleOwner, listenerBuilder: StateListener.() -> Unit) {
        observeState(null, null, owner, listenerBuilder)
    }

    fun observeState(
        statusView: View? = null,
        viewStatusCallback: ViewStatusCallback ?= null,
        owner: LifecycleOwner,
        listenerBuilder: StateListener.() -> Unit
    ) {
        val listener = StateListener().apply(listenerBuilder)
        val observer = object : BaseStateLiveDataObserver<T>(statusView) {

            override fun getEmptyCallback(): Class<out Callback>? {
                return statusView?.let {
                    if (viewStatusCallback == null) {
                        throw IllegalArgumentException("emptyCallback can not be null when setting statusView")
                    }
                    viewStatusCallback.emptyCallback
                }
            }

            override fun getErrorCallback(): Class<out Callback>? {
                return statusView?.let {
                    if (viewStatusCallback == null) {
                        throw IllegalArgumentException("errorCallback can not be null when setting statusView")
                    }
                    viewStatusCallback.errorCallback
                }
            }

            override fun getLoadingCallback(): Class<out Callback>? {
                return statusView?.let {
                    if (viewStatusCallback == null) {
                        throw IllegalArgumentException("loadingCallback can not be null when setting statusView")
                    }
                    viewStatusCallback.loadingCallback
                }
            }

            override fun onSuccess(data: T) {
                listener.successAction?.invoke(data)
            }

            override fun onDataEmpty() {
                listener.emptyAction?.invoke()
            }

            override fun onError(msg: String) {
                listener.errorAction?.invoke(msg)
            }

            override fun onCompletion() {
                listener.completeAction?.invoke()
            }

            override fun onReload(v: View?) {
                listener.reloadAction?.invoke(v)
            }
        }
        super.observe(owner, observer)
    }

    inner class StateListener {

        internal var startAction: (() -> Unit)? = null
        internal var successAction: ((T) -> Unit)? = null
        internal var errorAction: ((String) -> Unit)? = null
        internal var emptyAction: (() -> Unit)? = null
        internal var completeAction: (() -> Unit)? = null
        internal var reloadAction: ((v: View?) -> Unit)? = null

        fun onStart(action: (() -> Unit)?) {
            startAction = action
        }

        fun onSuccess(action: ((T) -> Unit)?) {
            successAction = action
        }

        fun onError(action: ((String) -> Unit)?) {
            errorAction = action
        }

        fun onEmpty(action: (() -> Unit)?) {
            emptyAction = action
        }

        fun onCompletion(action: (() -> Unit)?) {
            completeAction = action
        }

        fun onReload(action: ((View?) -> Unit)?) {
            reloadAction = action
        }
    }

}