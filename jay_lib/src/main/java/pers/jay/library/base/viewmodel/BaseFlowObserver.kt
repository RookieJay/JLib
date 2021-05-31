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
    open fun onFlowStart(baseResponse: BaseResponse<T>) {

    }

    /**
     * @desc    捕获上游出现的异常
     * @param   e 异常
     * @return  Unit
     */
    open fun onFlowCatch(e: Throwable) {
        e.errorHandle(e, customErrorHandle = {errorReason ->
            val errorMsg = errorReason.handleException(errorReason)
            Log.e("onFlowCatch", "errorMsg=$errorMsg")
        })
    }

    /**
     * @desc   请求完成
     * @param
     * @return Unit
     */
    open fun onFLowCompletion() {

    }

}