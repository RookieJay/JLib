package pers.jay.library.base

/**
 * @Author RookieJay
 * @Time 2021/9/27 15:23
 * @Description 状态监听器
 */
class StateListener<T> {

    var createAction: (() -> Unit)? = null
    var startAction: (() -> Unit)? = null
    var successAction: ((T?) -> Unit)? = null
    var resultAction: ((T) -> Unit)? = null
    var errorAction: ((String) -> Unit)? = null
    var emptyAction: (() -> Unit)? = null
    var completeAction: (() -> Unit)? = null

    fun onCreate(action: (() -> Unit)?) {
        createAction = action
    }

    fun onStart(action: (() -> Unit)?) {
        startAction = action
    }

    fun onSuccess(action: ((T?) -> Unit)?) {
        successAction = action
    }

    fun onResult(action: ((T) -> Unit)?) {
        resultAction = action
    }

    fun onError(action: ((String) -> Unit)?) {
        errorAction = action
    }

    fun onEmpty(action: (() -> Unit)?) {
        emptyAction = action
    }

    fun onCompletion(action: (() -> Unit)?) {
        completeAction = action
    }
    
}