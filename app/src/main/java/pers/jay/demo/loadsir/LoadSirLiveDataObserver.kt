package pers.jay.demo.loadsir

import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.Convertor
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import pers.jay.library.network.BaseResponse
import pers.jay.library.network.BaseResponse.DataState

/**
 * @Author RookieJay
 * @Time 2021/5/27 13:44
 * @Description 包含状态分发的LiveData Observer基类，子类需要继承并实现LoadSir的各种Callback
 * 主要结合LoadSir，根据BaseResp里面的State分别加载不同的UI，如Loading，Error
 * 注意：需要重写相应[Callback]实现自定义状态视图。
 */
abstract class LoadSirLiveDataObserver<T>(statusView: View?) : Observer<BaseResponse<T>>,
    Callback.OnReloadListener {

    protected var mLoadService: LoadService<Any>? = null

    companion object {
        private val TAG = LoadSirLiveDataObserver::class.java.simpleName
    }

    init {
        // 实例化view传空时，代表不走状态视图加载逻辑
        statusView?.apply {
            mLoadService = LoadSir.getDefault().register(this, this@LoadSirLiveDataObserver,
                Convertor<BaseResponse<T>> { response ->
                    val callbackClazz: Class<out Callback>? = when (response?.dataState) {
                        //数据刚开始请求，loading
                        DataState.CREATE, DataState.LOADING ->
                            getLoadingCallback()
                        //请求成功
                        DataState.SUCCESS ->
                            SuccessCallback::class.java
                        //数据为空
                        DataState.EMPTY ->
                            getEmptyCallback()
                        //请求失败
                        DataState.REQUEST_ERROR, DataState.BUSS_ERROR, DataState.UNKNOWN -> {
                            getErrorCallback()
                        }
                        else -> {
                            SuccessCallback::class.java
                        }
                    }
                    Log.d(TAG, "callbackClazz :${callbackClazz?.simpleName}")
                    callbackClazz
                })
        }

    }

    open fun getErrorCallback(): Class<out Callback>? {
        return null
    }

    open fun getEmptyCallback(): Class<out Callback>? {
        return null
    }

    open fun getLoadingCallback(): Class<out Callback>? {
        return null
    }

    /**
     * @desc   LiveData数据发生改变时回调，只解析view层需要单独处理的，统一处理的(loading)等则不解析
     * @param  response 返回的新数据
     * @return Unit
     */
    override fun onChanged(response: BaseResponse<T>?) {
        response?.apply {
            val dataState = response.dataState
            Log.d(TAG, "onChanged dataState: $dataState")
            when (dataState) {
                DataState.SUCCESS -> {
                    //请求成功，数据不为null
                    onSuccess(response.data!!)
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
                DataState.COMPLETED -> {
                    // 请求结束（无论成功/失败/异常）
                    onCompletion()
                }
                else -> {

                }
            }
            // 加载不同状态界面，最终转化为实际ui操作
            mLoadService?.showWithConvertor(response)
        }
    }

    /**
     * 请求成功且数据不为空
     */
    open fun onSuccess(data: T) {

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

    /**
     * @desc   点击重试时回调
     * @param  v 点击的view
     * @return Unit
     */
    override fun onReload(v: View?) {

    }

}