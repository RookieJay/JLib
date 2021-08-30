package pers.jay.library.loadsir

import com.kingja.loadsir.callback.Callback

/**
 * @Author RookieJay
 * @Time 2021/8/30 14:27
 * @Description 视图状态码，以Class作为标记，见[LoadSir]实现
 */
data class ViewStatusCallback(
    var loadingCallback: Class<out Callback>,
    var emptyCallback: Class<out Callback>,
    var errorCallback: Class<out Callback>
)