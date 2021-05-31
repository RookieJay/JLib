package pers.jay.library.base.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import pers.jay.library.base.livedata.SingleLiveData
import pers.jay.library.base.livedata.StateLiveData
import pers.jay.library.base.repository.BaseRepository
import pers.jay.library.base.repository.DataState
import pers.jay.library.network.BaseResponse
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
     * ui异常处理
     */
    private val mUiErrorData: SingleLiveData<String> = SingleLiveData()

    /**
     * Model实例
     */
    lateinit var mRepo: M

//    val mModel by lazy {
//        initModel()
//    }

    init {
        initModelByReflect()
    }

    fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                Log.e(TAG, e.message!!)
                mUiErrorData.postValue(e.message)
                cancel("launchOnUI exception occurred, msg:${e.message}")
            }
        }
    }

    protected fun launchOnIO(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            block()
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

    protected fun asyncIO(vararg blocks: suspend CoroutineScope.() -> Unit) {
        launchOnIO {
            async {
                for (block in blocks) {
                    block()
                }
            }
        }
    }

    private fun initModelByReflect() {
        val type = javaClass.genericSuperclass as ParameterizedType
        val modelClass = type.actualTypeArguments[0] as Class<M>
        mRepo = modelClass.newInstance()
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
    protected fun <T> createStateLiveEvent(requestBlock: suspend CoroutineScope.(StateLiveData<T>) -> BaseResponse<T>): StateLiveData<T> {
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

    /**
     * @desc   使用协程FLow进行通用请求，对每一个环节进行封装。统一处理
     * @param  flow Flow 冷的异步数据流，必须要observe才会发送
     * @return Unit
     */
    protected fun <T> requestOnFlow(
        flow: Flow<BaseResponse<T>>,
        flowObserver: BaseFlowObserver<T>
    ): StateLiveData<T> {
        val stateLiveData = createStateLiveData<T>()
        launchOnUI {
            var baseResponse = BaseResponse<T>()
            flow.onStart {
                // 请求开始，修改状态为Loading
                baseResponse.dataState = DataState.STATE_LOADING
                flowObserver.onFlowStart(baseResponse)
                stateLiveData.postValue(baseResponse)
            }
                .catch { e ->
                    // 请求失败，修改状态为Error
                    baseResponse.dataState = DataState.STATE_ERROR
                    baseResponse.msg = e.message
                    flowObserver.onFlowCatch(e)
                    stateLiveData.postValue(baseResponse)
                }
                .onCompletion {
                    // 请求结束，对返回数据进行判断，返回相应状态
//                    baseResponse.dataState = DataState.STATE_COMPLETED
                    flowObserver.onFLowCompletion()
                    stateLiveData.postValue(baseResponse)
                }
                .collect { response ->
                    run {
                        baseResponse = response
                        if (baseResponse.data == null
                            || baseResponse.data is List<*> && (baseResponse.data as List<*>).size == 0
                        ) {
                            // 数据为空，修改状态为Empty
                            baseResponse.dataState = DataState.STATE_EMPTY
                        } else {
                            baseResponse.dataState = DataState.STATE_SUCCESS
                        }
                        stateLiveData.postValue(baseResponse)
                    }
                }
        }
        return stateLiveData

    }

}