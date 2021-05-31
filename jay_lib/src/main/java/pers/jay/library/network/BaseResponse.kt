package pers.jay.library.network

import pers.jay.library.base.repository.DataState

/**
 * @Author RookieJay
 * @Time 2021/5/27 13:37
 * @Description 统一返回数据封装实体
 */
open class BaseResponse<T> {

    var code: Int = -999
    var msg: String? = null
    var data: T? = null

    // 数据状态
    var dataState: DataState? = null

    // 是否请求成功，根据业务自己重写get方法
    open val isSuccessful: Boolean
        get() = code == 0
}