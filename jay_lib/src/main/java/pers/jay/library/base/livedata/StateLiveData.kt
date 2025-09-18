package pers.jay.library.base.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import pers.jay.library.base.StateListener
import pers.jay.library.network.BaseResponse
import pers.jay.library.network.errorhandle.BussException

/**
 * @Author RookieJay
 * @Time 2021/5/31 10:39
 * @Description 含有数据状态的LiveData。
 * 封装了[BaseResponse], 以及封装[observeState]方法，使用Kotlin DSL 消除回调，使代码更具统一性。
 */
open class StateLiveData<T> : MutableLiveData<BaseResponse<T>>() {

    /**
     * 业务异常处理函数（在返回原始数据的一开始被调用），用于处理数据已返回，但返回数据有可能产生业务异常的场景。
     */
    var bussErrorHandle: ((BaseResponse<T>) -> BussException?)? = null

    /**
     * 数据预处理操作（在回调成功数据之前被调用），返回处理后的数据
     */
    var preDataHandle: ((BaseResponse<T>) -> T)? = null


    fun observeState(
        owner: LifecycleOwner,
        listenerBuilder: StateListener<T>.() -> Unit
    ) {
        val listener = StateListener<T>().apply(listenerBuilder)
        val observer = object : BaseStateLiveDataObserver<T>() {

            override fun onRequest() {
                listener.startAction?.invoke()
            }

            override fun onSuccess(data: T?) {
                listener.successAction?.invoke(data)
            }

            override fun onResult(data: T) {
                listener.resultAction?.invoke(data)
            }

            override fun onDataEmpty() {
                listener.emptyAction?.invoke()
            }

            override fun onError(throwable: Throwable) {
                listener.errorAction?.invoke(throwable)
            }

            override fun onErrorWithMessage(msg: String) {
                listener.errorActionWithMessage?.invoke(msg)
            }

            override fun onCompletion() {
                listener.completeAction?.invoke()
            }

        }
        super.observe(owner, observer)
    }

    /**
     * 更新数据状态并post
     */
    fun setResponse(response: BaseResponse<T>) {
        value = response
    }

    fun <T> StateLiveData<T>.bussErrorHandle(bussErrorHandle: (BaseResponse<T>) -> BussException?): StateLiveData<T> {
        this.bussErrorHandle = bussErrorHandle
        return this
    }


    fun <T> StateLiveData<T>.preDataHandle(
        dataHandle: ((BaseResponse<T>) -> T)
    ): StateLiveData<T> {
        this.preDataHandle = dataHandle
        return this
    }

}