package pers.jay.library.loadsir

import com.kingja.loadsir.callback.Callback

/**
 * @Author RookieJay
 * @Time 2021/8/30 14:27
 * @Description 视图状态码，以Class作为标记，见[LoadSir]实现。根据不同业务，外部实例化此类以传入不同的状态回调
 */
data class StatusCallback(
    var loadingCallback: Class<out Callback>,
    var emptyCallback: Class<out Callback>,
    var errorCallback: Class<out Callback>,
    var retryCallback: Class<out Callback>,
)