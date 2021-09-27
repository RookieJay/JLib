package pers.jay.library.base.viewmodel

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import pers.jay.library.base.StateListener
import pers.jay.library.base.livedata.SingleLiveData
import pers.jay.library.base.livedata.StateLiveData
import pers.jay.library.base.repository.BaseRepository
import pers.jay.library.base.repository.DataState
import pers.jay.library.network.BaseResponse
import pers.jay.library.network.coroutine.errorHandle
import pers.jay.library.network.coroutine.handleException
import java.lang.reflect.ParameterizedType

/**
 * @Author RookieJay
 * @Time 2021/5/27 15:26
 * @Description 基于ViewModel封装基类，使用协程进行线程切换
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseViewModel<M : BaseRepository> : ViewModel(), IViewModel {

    protected val TAG = javaClass::class.simpleName

    /**
     * 协程异常处理
     */
    val mCoroutineErrorData: SingleLiveData<String> = SingleLiveData()

    /**
     * Model实例
     */
    val mRepo: M by lazy {
        initModelByReflect()
    }

    private fun initModelByReflect(): M {
        val type = javaClass.genericSuperclass as ParameterizedType
        val modelClass = type.actualTypeArguments[0] as Class<M>
        return modelClass.newInstance()
    }

    fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                Log.e(TAG, "launchOnUI error:${e.message}")
                mCoroutineErrorData.postValue(e.message)
                cancel("launchOnUI exception occurred, msg:${e.message}")
            }
        }
    }

    protected fun launchOnIO(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            try {
                block()
            } catch (e: Exception) {
                Log.e(TAG, "launchOnIO error:${e.message}")
                mCoroutineErrorData.postValue(e.message)
                cancel("launchOnIO exception occurred, msg:${e.message}")
            }
        }
    }

    /**
     * 串行执行IO任务
     */
    protected suspend fun withContextIo(block: suspend CoroutineScope.() -> Unit) {
        withContext(Dispatchers.IO, block)
    }

    /**
     * 串行执行Main任务
     */
    protected suspend fun withContextMain(block: suspend CoroutineScope.() -> Unit) {
        withContext(Dispatchers.Main, block)
    }

    /**
     * 批量异步执行IO任务
     */
    protected fun asyncIO(vararg blocks: suspend CoroutineScope.() -> Unit) {
        launchOnIO {
            async {
                for (block in blocks) {
                    block()
                }
            }
        }
    }

    protected fun <T> createSingleLiveEvent(): SingleLiveData<T> {
        return SingleLiveData<T>()
    }

    /**
     * 创建一个简单的事件并发送数据
     * @param requestBlock 请求函数体，最后一行表示返回数据，直接通过LiveData发送
     */
    protected fun <T> createSimpleEvent(requestBlock: suspend CoroutineScope.(SingleLiveData<T>) -> T): SingleLiveData<T> {
        val event = createSingleLiveEvent<T>()
        launchOnIO {
            event.postValue(requestBlock(event))
        }
        return event
    }

    /**
     * 创建一个简单的事件并发送数据
     * @param requestBlock 请求函数体，最后一行表示返回数据，直接通过LiveData发送
     */
    open fun <T> createStateLiveEvent(requestBlock: suspend CoroutineScope.(StateLiveData<T>) -> BaseResponse<T>): StateLiveData<T> {
        val stateLiveData = createStateLiveData<T>()
        launchOnIO {
            stateLiveData.postValue(requestBlock(stateLiveData))
        }
        return stateLiveData
    }

    /**
     * 创建一个新的StateLiveData
     */
    protected fun <T> createStateLiveData(): StateLiveData<T> {
        return StateLiveData()
    }

    protected fun <T> requestWithFlow(
        flow: Flow<BaseResponse<T>>,
        listenerBuilder: StateListener<T>.() -> Unit
    ): StateLiveData<T> {
        return requestWithFlow(true, flow, listenerBuilder)
    }

    /**
     *  结合[BaseRepository.createFlowRequest],使用协程FLow进行通用请求，对每一个环节进行封装。
     *  此方法既能将状态通过stateObserver回调到viewModel层，又能通过StateLiveData通知view层。
     *
     * @param  fetchData 是否需要获取数据，默认为true，若无需获取数据请传入false
     * @param  flow Flow 冷的异步数据流，必须要observe才会发送
     * @param  listenerBuilder 可空。通用数据变化的观察者，可对每个步骤进行统一处理，交由viewModel处理
     *
     * @return [StateLiveData] 含有数据状态的LiveData，封装了[BaseResponse]
     */
    private fun <T> requestWithFlow(
        fetchData: Boolean = true,
        flow: Flow<BaseResponse<T>>,
        listenerBuilder: StateListener<T>.() -> Unit
    ): StateLiveData<T> {
        val stateLiveData = createStateLiveData<T>()
        val listener = StateListener<T>().also(listenerBuilder)
        launchOnUI {
            var response = BaseResponse<T>()
            flow.onStart {
                // 请求开始，修改状态为Loading
                response.dataState = DataState.STATE_LOADING
                stateLiveData.value = response
                listener.startAction?.invoke()
            }
                // catch 函数只是中间操作符,只能捕获它的上游的异常,不能捕获下游的异常，类似 collect 内的异常
                // onCompletion在catch操作符后，则 catch 操作符捕获到异常后，不会影响到下游
                .catch { e ->
                    LogUtils.e(TAG, "catch, $e")
                    // 请求失败，修改状态为Error
                    e.errorHandle(e, customErrorHandle = { errorReason ->
                        val errorMsg = errorReason.handleException(errorReason)
                        Log.e("onCatch", "errorMsg=$errorMsg")
                        response.dataState = DataState.STATE_ERROR
                        response.errorReason = errorMsg
                        stateLiveData.value = response
                        listener.errorAction?.invoke(errorMsg)
                    })
                }
                .onCompletion { cause ->
                    LogUtils.d(TAG, "onCompletion，$cause")
                    // 请求结束，修改状态为Completed
//                    response.dataState = DataState.STATE_COMPLETED
//                    stateLiveData.value = response
                    listener.completeAction?.invoke()
                }
                .flowOn(Dispatchers.Main)
                .collectLatest {
                    response = it
                    if (!response.isSuccessful) {
                        // 服务器返回失败，修改状态为Error
                        LogUtils.e(TAG, "request successfully but response error from server,baseResponse class=$response")
                        response.dataState = DataState.STATE_FAILED
                        response.errorReason = response.msg
                        listener.errorAction?.invoke(if (TextUtils.isEmpty(response.errorReason)) "请求失败" else response.errorReason!! )
                    } else if (response.data == null
                        || (response.data is List<*> && (response.data as List<*>).isEmpty())
                    ) {
                        // 数据为空，修改状态为Empty
                        if (fetchData) {
                            response.dataState = DataState.STATE_EMPTY
                            listener.emptyAction?.invoke()
                        }
                    } else {
                        // 请求成功切数据正确，修改状态为Success
                        response.dataState = DataState.STATE_SUCCESS
                        listener.successAction?.invoke(response.data!!)
                    }
                    stateLiveData.value = response
                }
        }
        return stateLiveData
    }

//    class StateListener<T> {
//
//        internal var startAction: (() -> Unit)? = null
//        internal var successAction: ((T) -> Unit)? = null
//        internal var errorAction: ((String) -> Unit)? = null
//        internal var emptyAction: (() -> Unit)? = null
//        internal var completeAction: (() -> Unit)? = null
//
//        fun onStart(action: (() -> Unit)?) {
//            startAction = action
//        }
//
//        fun onSuccess(action: ((T) -> Unit)?) {
//            successAction = action
//        }
//
//        fun onError(action: ((String) -> Unit)?) {
//            errorAction = action
//        }
//
//        fun onEmpty(action: (() -> Unit)?) {
//            emptyAction = action
//        }
//
//        fun onCompletion(action: (() -> Unit)?) {
//            completeAction = action
//        }
//    }

}