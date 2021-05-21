package pers.jay.library.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import pers.jay.library.network.coroutine.errorHandle
import java.lang.reflect.ParameterizedType

/**
 * 基于ViewModel封装基类，使用协程进行线程切换
 *
 * 协程理解：https://www.bilibili.com/video/BV1KJ41137E9/?spm_id_from=333.788.recommend_more_video.0
 *
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseViewModel<M : BaseModel> : ViewModel() {

    protected val TAG = javaClass::class.simpleName

    /**
     * 协程异常处理
     */
    val mCoroutineErrorData: SingleLiveEvent<String> = SingleLiveEvent()

    /**
     * ui异常处理
     */
    private val mUiErrorData: SingleLiveEvent<String> = SingleLiveEvent()

    /**
     * Model实例
     */
    lateinit var mModel: M

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
            try {
                block()
            } catch (e: Exception) {
                e.errorHandle(e) { code, msg ->
                    //后台自定义异常处理
                    mCoroutineErrorData.postValue(msg)
                    cancel("launchOnIO exception occurred, msg:${msg}")
                }
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
        mModel = modelClass.newInstance()
    }
    
    protected fun <T> createSingleLiveEvent(): SingleLiveEvent<T> {
        return SingleLiveEvent<T>()
    }

    /**
     * 创建一个简单的事件并发送数据
     * @param requestBlock 请求函数体，最后一行表示返回数据，直接通过LiveData发送
     */
    protected fun <T> createSimpleEvent(requestBlock: suspend CoroutineScope.(SingleLiveEvent<T>) -> T): SingleLiveEvent<T> {
        val event =  createSingleLiveEvent<T>()
        launchOnIO {
            event.postValue(requestBlock(event))
        }
        return event
    }
}