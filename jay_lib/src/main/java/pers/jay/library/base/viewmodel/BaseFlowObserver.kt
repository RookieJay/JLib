package pers.jay.library.base.viewmodel

import android.util.Log
import pers.jay.library.network.BaseResponse
import pers.jay.library.network.coroutine.errorHandle
import pers.jay.library.network.coroutine.handleException

/**
 * @Author RookieJay
 * @Time 2021/5/27 16:42
 * @Description 通用Flow流的观察者，可对每个步骤进行统一处理，想单独处理则继承此类
 */
abstract class BaseFlowObserver<T> {

    /**
     * @desc    在调用 flow 请求数据之前，做一些准备工作，例如显示正在加载数据的进度条
     * @param
     * @return  Unit
     */
    open fun onStart(baseResponse: BaseResponse<T>) {

    }

    /**
     * @Author RookieJay
     * @Time 2021/6/1 13:46
     * @Description 请求成功且数据正确
     */
    open fun onSuccess(response: BaseResponse<T>) {

    }

    /**
     * @desc    捕获上游出现的异常
     * @param   e 异常
     * @return  Unit
     */
    open fun onCatch(e: Throwable) {
        e.printStackTrace()
        e.errorHandle(e, customErrorHandle = {errorReason ->
            val errorMsg = errorReason.handleException(errorReason)
            Log.e("onCatch", "errorMsg=$errorMsg")
            onError(errorMsg)
        })
    }

    /**
     * @desc   错误信息回调
     * @param  msg 错误信息
     * @return Unit
     */
    open fun onError(msg: String) {

    }

    /**
     * @desc   请求完成
     * @param
     * @return Unit
     */
    open fun onCompletion() {

    }

}