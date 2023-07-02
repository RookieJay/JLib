package pers.jay.demo.common

import pers.jay.demo.loadsir.EmptyCallback
import pers.jay.demo.loadsir.ErrorCallback
import pers.jay.demo.loadsir.LoadingCallback
import pers.jay.demo.loadsir.RetryCallback
import pers.jay.demo.loadsir.StatusCallback

object Const {

    @JvmStatic
    val DEFAULT_VIEW_STATUS_CALLBACK = StatusCallback(
        LoadingCallback::class.java,
        EmptyCallback::class.java,
        ErrorCallback::class.java,
        RetryCallback::class.java
    )

    @JvmStatic
    val DEFAULT_ACTIVITY_STATUS_CALLBACK = StatusCallback(
        LoadingCallback::class.java,
        EmptyCallback::class.java,
        ErrorCallback::class.java,
        RetryCallback::class.java
    )
}