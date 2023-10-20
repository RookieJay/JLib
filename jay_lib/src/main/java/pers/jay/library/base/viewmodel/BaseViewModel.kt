package pers.jay.library.base.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import pers.jay.library.base.StateListener
import pers.jay.library.base.livedata.SingleLiveData
import pers.jay.library.base.livedata.StateLiveData
import pers.jay.library.base.repository.BaseRepository
import pers.jay.library.network.BaseResponse
import pers.jay.library.network.coroutine.getRequestError
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
     * 创建一个新的StateLiveData
     */
    protected fun <T> createStateLiveData(): StateLiveData<T> {
        return StateLiveData()
    }

    /**
     * @desc   通过flow来处理请求，并返回一个Flow对象 todo  后续优化，可考虑直接从Retrofit返回一个Flow对象
     * @param  requestBlock 请求方法体，返回值为T
     * @return [Flow]
     */
    open fun <T> createFlowRequest(requestBlock: suspend () -> T): Flow<T> {
        return flow {
            val response = requestBlock.invoke()
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 发起请求，带有数据返回（自动对第一层数据做空校验并回调，非空数据在onResult回调）
     */
    public fun <T> Flow<BaseResponse<T>>.requestData(
        exStateLiveData: StateLiveData<T>? = null,
        listenerBuilderBlock: StateListener<T>.() -> Unit
    ): StateLiveData<T> {
        return requestWithState(true, exStateLiveData, listenerBuilderBlock)
    }

    /**
     * 发起请求（不关心回调数据是否可空）
     */
    protected fun <T> Flow<BaseResponse<T>>.request(
        exStateLiveData: StateLiveData<T>? = null,
        listenerBuilderBlock: StateListener<T>.() -> Unit
    ): StateLiveData<T> {
        return requestWithState(false, exStateLiveData, listenerBuilderBlock)
    }

    /**
     *  使用协程flow进行通用请求，对每一个环节进行封装。同时通知到viewModel和view层，按需处理各种状态(@see[BaseResponse.DataState])
     *
     *  eg.
     *      flow<BaseResponse<String>> { }.requestWithState {
     *           onStart {  }
     *           onSuccess {  }
     *           onError {  }
     *           .....
     *       }
     *
     * @param exStateLiveData 外部按需传入StateLiveData，可用于监听数据变化，避免重复创建对象。
     * @param requireData 是否需要返回数据，回调onSuccess(T?)->onResult(T); 若不需要，则只回调onSuccess(T?)。默认为true。
     *        请注意：若不传此参数或传入true，会默认自动添加判空逻辑，应确认需要的是非空数据，否则当返回null时，会回调onEmpty()。否则应该传入false。
     * @param listenerBuilderBlock 可空。通用数据变化的观察者，可对每个步骤进行统一处理，交由viewModel处理
     * @param T 最终要展示到ui的数据类型
     *
     * @return [StateLiveData] 含有数据状态的LiveData，回调数据状态到ui层。封装了[BaseResponse]
     *
     */
    private fun <T> Flow<BaseResponse<T>>.requestWithState(
        requireData: Boolean? = true,
        exStateLiveData: StateLiveData<T>? = null,
        listenerBuilderBlock: StateListener<T>.() -> Unit
    ): StateLiveData<T> {
        val stateLiveData = exStateLiveData ?: createStateLiveData()
        var stateResponse = stateLiveData.value ?: BaseResponse()
        stateResponse.dataState = BaseResponse.DataState.LOADING
        val listener: StateListener<T> = StateListener<T>().apply(listenerBuilderBlock)
        val requestFlow = this.onStart {
            // 请求开始，修改状态为Loading
            listener.startAction?.invoke()
            stateResponse.dataState = BaseResponse.DataState.LOADING
            stateLiveData.updateState(stateResponse)
        }
        viewModelScope.launch(Dispatchers.Main) {
            // catch 函数只是中间操作符,只能捕获它的上游的异常,不能捕获下游的异常，类似 collect 内的异常
            requestFlow.catch { e ->
                Log.e(TAG, "flow catch: $e")
                e.printStackTrace()
                val errorMessage = e.getRequestError()
                // 请求失败，修改状态为Error
                listener.errorAction?.invoke(errorMessage)
                stateResponse.errorReason = errorMessage
                stateResponse.dataState = BaseResponse.DataState.REQUEST_ERROR
                stateLiveData.updateState(stateResponse)
            }.onCompletion { cause ->
                Log.i(TAG, "onCompletion，$cause")
                // 请求正常结束，回调完成
                listener.completeAction?.invoke()
            }.collectLatest {
                // 请求返回
                stateResponse = it
                val data: T? = stateResponse.data
                kotlin.runCatching {
                    if (requireData == true && data == null) {
                        //1、若需要获取数据，自动添加判空逻辑。流程不再向下执行。
                        listener.emptyAction?.invoke()
                        stateResponse.dataState = BaseResponse.DataState.EMPTY
                        stateLiveData.updateState(stateResponse)
                        return@collectLatest
                    }
                    // 2、检查返回是否有业务异常需要处理,若有，则取手动修改的BaseResponse状态，并返回，更新状态，以通知view层改变。流程不再向下执行。
                    val isSuccessful = stateResponse.isSuccessful()
                    val handleBussError = stateLiveData.bussErrorHandle?.invoke(stateResponse)
                    if (!isSuccessful || handleBussError == true) {
                        stateResponse.dataState = BaseResponse.DataState.BUSS_ERROR
                        listener.errorAction?.invoke(stateResponse.errorReason ?: BaseResponse.DataState.BUSS_ERROR.value())
                        stateLiveData.updateState(stateResponse)
                        return@collectLatest
                    }
                    // 3、数据预处理逻辑，有则执行,并返回经过处理的数据。
                    // 应用场景：viewModel在返回默认数据之前需要拦截原始响应做处理，如做数据处理、缓存等。
                    val resultData: T? = stateLiveData.preDataHandle?.let {
                        stateLiveData.preDataHandle?.invoke(stateResponse)
                    } ?: data
                    // 4、成功响应回调（数据可空）
                    listener.successAction?.invoke(resultData)
                    stateResponse.data = resultData
                    stateResponse.dataState = BaseResponse.DataState.SUCCESS
                    stateLiveData.updateState(stateResponse)

                    // 5、非空数据回调
                    listener.resultAction?.invoke(resultData!!)
                    stateResponse.data = resultData
                    stateResponse.dataState = BaseResponse.DataState.DATA_RESULT
                    stateLiveData.updateState(stateResponse)
                }.onFailure { e ->
                    // 6、数据处理过程中发生的异常捕获处理，错误回调
                    val errorMsg = "exception occurred in flow collect:[${e.message}]"
                    Log.e(TAG, errorMsg)
                    e.printStackTrace()
                    listener.errorAction?.invoke(errorMsg)
                    stateResponse.errorReason = errorMsg
                    stateResponse.dataState = BaseResponse.DataState.BUSS_ERROR
                    stateLiveData.updateState(stateResponse)
                }
            }
        }
        return stateLiveData
    }
}