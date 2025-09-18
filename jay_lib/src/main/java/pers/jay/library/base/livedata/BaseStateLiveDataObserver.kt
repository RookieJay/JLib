package pers.jay.library.base.livedata

import android.util.Log
import androidx.lifecycle.Observer
import pers.jay.library.network.BaseResponse
import pers.jay.library.network.BaseResponse.DataState
import pers.jay.library.network.coroutine.getRequestErrorMsg

/**
 * @Author RookieJay
 * @Time 2021/5/27 13:44
 * @Description 包含状态分发的LiveData Observer基类，根据BaseResp回调的数据状态触发相应回调
 */
abstract class BaseStateLiveDataObserver<T>() : Observer<BaseResponse<T>> {


    companion object {
        private val TAG = BaseStateLiveDataObserver::class.java.simpleName
    }


    /**
     *  LiveData数据发生改变时回调，只解析view层需要单独处理的，统一处理的(loading)等则不解析
     * @param  response 返回的新数据
     * @return Unit
     */
    override fun onChanged(response: BaseResponse<T>?) {
        Log.d(TAG, "onChanged response=${response.hashCode()}, $response")
        response?.apply {
            val dataState = response.dataState
            Log.d(TAG, "onChanged dataState: $dataState")
            when (dataState) {
                DataState.LOADING -> {
                    //请求中
                    onRequest()
                }
                DataState.SUCCESS -> {
                    //请求成功，数据可能为null
                    onSuccess(response.data)
                }
                DataState.DATA_RESULT -> {
                    //请求成功，数据不为null
                    onResult(response.data!!)
                }
                DataState.EMPTY -> {
                    //数据为空
                    onDataEmpty()
                }
                DataState.REQUEST_ERROR -> {
                    // 请求错误
                    val error = response.error ?: Exception("request error")
                    onError(error)
                    // 给出友好提示，原message可从onError获取
                    onErrorWithMessage(error.getRequestErrorMsg())
                }
                DataState.BUSS_ERROR -> {
                    // 业务异常
                    val error = response.error ?: Exception("buss error")
                    onError(error)
                    onErrorWithMessage(error.message.toString())
                }
                DataState.COMPLETED -> {
                    // 请求结束（无论成功/失败/异常）
                    onCompletion()
                }
                else -> {
                }
            }
        }
    }

    /**
     * 请求中
     */
    open fun onRequest() {

    }

    /**
     * 请求成功且数据可能为空
     */
    open fun onSuccess(data: T?) {

    }

    /**
     * 请求成功且数据不为空
     */
    open fun onResult(data: T) {

    }

    /**
     * 请求成功，但数据为空
     */
    open fun onDataEmpty() {

    }

    /**
     * 请求错误
     */
    open fun onErrorWithMessage(msg: String) {

    }

    open fun onError(throwable: Throwable) {

    }

    /**
     * 请求完成
     */
    open fun onCompletion() {

    }

}