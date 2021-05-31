package pers.jay.library.base.livedata

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.Convertor
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import pers.jay.library.base.repository.DataState
import pers.jay.library.network.BaseResponse

/**
 * @Author RookieJay
 * @Time 2021/5/27 13:44
 * @Description 包含状态分发的LiveData Observer基类，子类需要继承并实现LoadSir的各种Callback
 * 主要结合LoadSir，根据BaseResp里面的State分别加载不同的UI，如Loading，Error
 * 开发者可以在UI层，每个接口请求时，直接创建IStateObserver，重写相应[Callback]。
 */
abstract class BaseStateLiveDataObserver<T>(view: View?) : Observer<BaseResponse<T>>,
    Callback.OnReloadListener {

    protected var mLoadService: LoadService<Any>? = null

    companion object {
        private val TAG = BaseStateLiveDataObserver::class.java.simpleName
    }

    init {
        if (view != null) {
            mLoadService = LoadSir.getDefault().register(view, this,
                Convertor<BaseResponse<T>> { response ->
                    val callbackClazz: Class<out Callback> = when (response?.dataState) {
                        //数据刚开始请求，loading
                        DataState.STATE_CREATE, DataState.STATE_LOADING ->
                            getLoadingCallback()
                        //请求成功
                        DataState.STATE_SUCCESS ->
                            SuccessCallback::class.java
                        //数据为空
                        DataState.STATE_EMPTY ->
                            getEmptyCallback()
                        //请求失败
                        DataState.STATE_FAILED, DataState.STATE_ERROR -> {
                            getErrorCallback()
                        }
                        //请求完成
                        DataState.STATE_COMPLETED, DataState.STATE_UNKNOWN -> {
                            getLoadingCallback()
                        }
                        else -> {
                            SuccessCallback::class.java
                        }
                    }
                    Log.d(TAG, "callbackClazz :${callbackClazz.simpleName}")
                    callbackClazz
                })
        }

    }

    abstract fun getErrorCallback(): Class<out Callback>

    abstract fun getEmptyCallback(): Class<out Callback>

    abstract fun getLoadingCallback(): Class<out Callback>

    /**
     * @desc   LiveData数据发生改变时回调
     * @param  response 返回的新数据
     * @return Unit
     */
    override fun onChanged(response: BaseResponse<T>?) {
        response?.apply {
            Log.d(TAG, "onChanged: ${response.dataState}")
            when (response.dataState) {
                DataState.STATE_SUCCESS -> {
                    //请求成功，数据不为null
                    onSuccess(response.data)
                }
                DataState.STATE_EMPTY -> {
                    //数据为空
                    onDataEmpty()
                }
                DataState.STATE_FAILED, DataState.STATE_ERROR -> {
                    //请求错误
                    onError(response.msg)
                }
                else -> {

                }
            }
        }
//        Log.d(TAG, "onChanged: mLoadService $mLoadService")
        //加载不同状态界面
        mLoadService?.showWithConvertor(response)
    }

    /**
     * 请求成功且数据不为空
     */
    open fun onSuccess(data: T?) {

    }

    /**
     * 请求成功，但数据为空
     */
    open fun onDataEmpty() {

    }

    /**
     * 请求错误
     */
    open fun onError(msg: String?) {

    }

    /**
     * @desc   点击重试时回调
     * @param  v 点击的view
     * @return Unit
     */
    override fun onReload(v: View?) {

    }

}