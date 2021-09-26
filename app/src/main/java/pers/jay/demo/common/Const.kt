package pers.jay.demo.common

import pers.jay.demo.loadsir.EmptyCallback
import pers.jay.demo.loadsir.LoadingCallback
import pers.jay.demo.loadsir.RetryCallback
import pers.jay.library.loadsir.ViewStatusCallback

/**
 * Copyright (c) 2011, 北京视达科技有限责任公司 All rights reserved.
 * author：juncai.zhou
 * date：2021/9/26 14:44
 * description：
 */
object Const {

    @JvmStatic
    val DEFAULT_LOADSIR_CALLBACK = ViewStatusCallback(
        LoadingCallback::class.java,
        EmptyCallback::class.java,
        RetryCallback::class.java
    )
}