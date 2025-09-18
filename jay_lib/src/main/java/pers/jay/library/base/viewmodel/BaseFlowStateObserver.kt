package pers.jay.library.base.viewmodel

import android.util.Log
import pers.jay.library.network.BaseResponse
import pers.jay.library.network.coroutine.getRequestErrorMsg

/**
 * @Author RookieJay
 * @Time 2021/5/27 16:42
 * @Description 通用状态变化的观察者，用于对Flow每个步骤进行统一处理，若想单独处理则继承此类重写对应方法
 */
abstract class BaseFlowStateObserver<T> {

    companion object {
        protected val TAG: String = BaseFlowStateObserver::class.java.simpleName
    }

    /**
     * 在调用 flow 请求数据之前，做一些准备工作，例如显示正在加载数据的进度条
     * @param   response [BaseResponse]
     * @return  Unit
     */
    open fun onStart(response: BaseResponse<T>) {

    }

    /**
     * 请求成功且数据正确，成功返回数据，根据数据返回相应状态
     * @return Unit
     */
    open fun onSuccess(response: BaseResponse<T>) {

    }

    /**
     * 捕获上游出现的异常
     * @param   e 异常
     * @return  Unit
     */
    open fun onCatch(response: BaseResponse<T>, e: Throwable) {
        e.printStackTrace()
        val errorMsg = e.getRequestErrorMsg()
        Log.e("onCatch", "errorMsg=$errorMsg")
        onError(errorMsg)
    }

    /**
     * 错误信息回调
     * @param  msg 错误信息
     * @return Unit
     */
    open fun onError(msg: String) {

    }

    /**
     * 请求完成
     * @param
     * @return Unit
     */
    open fun onCompletion() {

    }

}