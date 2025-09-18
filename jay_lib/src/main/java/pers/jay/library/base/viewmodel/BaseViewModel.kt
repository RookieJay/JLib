package pers.jay.library.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pers.jay.library.base.StateListener
import pers.jay.library.base.livedata.SingleLiveData
import pers.jay.library.base.livedata.StateLiveData
import pers.jay.library.base.repository.BaseRepository
import pers.jay.library.network.BaseResponse
import pers.jay.library.network.coroutine.getRequestError
import pers.jay.library.network.errorhandle.BussException
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
    protected val mRepo: M by lazy {
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
                LogUtils.e(TAG, "launchOnUI error:${e.message}")
                cancel("launchOnUI exception occurred, msg:${e.message}")
            }
        }
    }

    protected fun launchOnIO(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            try {
                block()
            } catch (e: Exception) {
                LogUtils.e(TAG, "launchOnIO error:${e.message}")
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
     * 通过flow来处理请求，并返回一个Flow对象 todo  后续优化，可考虑直接从Retrofit返回一个Flow对象
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
        requireData: Boolean = true,
        exStateLiveData: StateLiveData<T>? = null,
        listenerBuilderBlock: StateListener<T>.() -> Unit
    ): StateLiveData<T> {
        val stateLiveData = exStateLiveData ?: createStateLiveData()
        val stateResponse = stateLiveData.value ?: BaseResponse()
        val listener: StateListener<T> = StateListener<T>().apply(listenerBuilderBlock)
        val requestFlow = this.onStart {
            // 请求开始，修改状态为Loading
            val newState = BaseResponse<T>().apply {
                dataState = BaseResponse.DataState.LOADING
            }
            stateLiveData.setResponse(newState)
            listener.startAction?.invoke()
        }
        viewModelScope.launch(Dispatchers.Main) {
            // catch 函数只是中间操作符,只能捕获它的上游的异常,不能捕获下游的异常，类似 collect 内的异常
            requestFlow.catch { e ->
                handleException(e, stateLiveData, listener)
            }.onCompletion { cause ->
                LogUtils.i(TAG, "onCompletion，$cause")
                // 请求正常结束，回调完成
                listener.completeAction?.invoke()
                stateResponse.dataState = BaseResponse.DataState.COMPLETED
                stateLiveData.setResponse(stateResponse)
            }.collectLatest {
                // 处理响应数据
                handleResponse(it, listener, stateLiveData, requireData)
            }
        }
        return stateLiveData
    }

    private fun <T> handleResponse(
        response: BaseResponse<T>,
        listener: StateListener<T>,
        stateLiveData: StateLiveData<T>,
        requireData: Boolean
    ) {
        // 请求返回
        val data: T? = response.data
        kotlin.runCatching {
            // 1、 先判断业务是否成功（简单根据状态码定义的成功或没有重写handleBussError返回的异常情况）
            checkBussError<T>(response, stateLiveData)
            //2、若需要获取数据，自动添加判空逻辑。流程不再向下执行。
            if (requireData && data == null) {
                listener.emptyAction?.invoke()
                response.dataState = BaseResponse.DataState.EMPTY
                stateLiveData.setResponse(response)
                return
            }
            // 3、数据预处理逻辑，有则执行,并返回经过处理的数据。
            // 应用场景：viewModel在返回默认数据之前需要拦截原始响应做处理，如做数据处理、缓存等。
            val preHandledData = stateLiveData.preDataHandle?.invoke(response)
            if (requireData && stateLiveData.preDataHandle != null && preHandledData == null) {
                // 需要数据返回且实现了预处理数据逻辑，但返回空的情况
                throw BussException(message = "requireData but preDataHandle return null")
            }
            val resultData = preHandledData ?: data
            // 4、成功响应回调（数据可空）
            listener.successAction?.invoke(resultData)
            response.data = resultData
            response.dataState = BaseResponse.DataState.SUCCESS
            stateLiveData.setResponse(response)

            // 5、非空数据回调
            if (requireData) {
                listener.resultAction?.invoke(resultData!!)
                response.data = resultData
                response.dataState = BaseResponse.DataState.DATA_RESULT
                stateLiveData.setResponse(response)
            }
        }.onFailure { e ->
            // 6、数据处理过程中发生的异常捕获处理，错误回调
            val errorMsg = "exception occurred when handleResponse:[${e.message}]"
            LogUtils.e(TAG, errorMsg)
            e.printStackTrace()
            handleException(e, stateLiveData, listener)
        }
    }

    /**
     * 处理异常情况
     */
    private fun <T> handleException(
        e: Throwable,
        stateLiveData: StateLiveData<T>,
        listener: StateListener<T>
    ) {
        LogUtils.e(TAG, "Flow error: $e")
        e.printStackTrace()
        val stateResponse = stateLiveData.value ?: BaseResponse<T>()
        when (e) {
            is BussException -> {
                // 业务异常
                val defBussErrorMsg = BaseResponse.DataState.BUSS_ERROR.value()
                val bussError = stateLiveData.bussErrorHandle?.invoke(stateResponse)
                if (bussError != null) {
                    val errorMsg = stateResponse.msg
                    listener.errorAction?.invoke(e)
                    listener.errorActionWithMessage?.invoke(if (errorMsg.isNullOrEmpty()) defBussErrorMsg else errorMsg)
                    stateResponse.apply {
                        dataState = BaseResponse.DataState.BUSS_ERROR
                        error = e
                    }
                    stateLiveData.setResponse(stateResponse)
                }
            }
            else -> {
                // 请求/处理数据异常
                val errorMessage = e.getRequestError()
                listener.errorAction?.invoke(e)
                listener.errorActionWithMessage?.invoke(errorMessage)
                stateResponse.apply {
                    error = e
                    dataState = BaseResponse.DataState.REQUEST_ERROR
                }
                stateLiveData.setResponse(stateResponse)
            }
         }

    }


    private fun <T> checkBussError(
        stateResponse: BaseResponse<T>,
        stateLiveData: StateLiveData<T>
    ): Boolean {
        val isSuccessful = stateResponse.isSuccessful()
        if (!isSuccessful) {
            LogUtils.e(TAG, "business failed: ${stateResponse.msg}")
            val errorMsg = stateResponse.msg
            throw BussException(message = errorMsg)
        }
        val bussException = stateLiveData.bussErrorHandle?.invoke(stateResponse)
        if (bussException != null) {
            LogUtils.e(TAG, "custom business failed: ${bussException.message}")
            throw bussException
        }
        return false
    }
}