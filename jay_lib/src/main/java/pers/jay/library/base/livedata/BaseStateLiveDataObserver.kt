package pers.jay.library.base.livedata

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.Observer
import pers.jay.library.network.BaseResponse
import pers.jay.library.network.BaseResponse.DataState

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
     * @desc   LiveData数据发生改变时回调，只解析view层需要单独处理的，统一处理的(loading)等则不解析
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
                DataState.REQUEST_ERROR, DataState.BUSS_ERROR -> {
                    //请求错误
                    var reason = response.errorReason
                    if (TextUtils.isEmpty(reason)) {
                        reason = dataState.value()
                    }
                    onError(reason!!)
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
    open fun onError(msg: String) {

    }

    /**
     * 请求完成
     */
    open fun onCompletion() {

    }

}