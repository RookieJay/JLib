package pers.jay.library.network

import com.squareup.moshi.JsonClass
import pers.jay.library.base.repository.DataState

/**
 * @Author RookieJay
 * @Time 2021/5/27 13:37
 * @Description 统一返回数据封装实体, 按需对各open属性进行重写
 */
@JsonClass(generateAdapter = true)
open class BaseResponse<T> {

    open var code: Int = -999
    open var msg: String? = null
    open var data: T? = null

    // 数据状态
    var dataState: DataState? = null

    // 错误原因
    var errorReason: String? = null

    // 是否请求成功，根据业务自己重写get方法
    open val isSuccessful: Boolean
        get() = code == 0

    override fun toString(): String {
        return "BaseResponse(code=$code, msg=$msg, data=${data}, dataState=$dataState, errorReason=$errorReason)"
    }


}