package pers.jay.demo.net

import com.squareup.moshi.Json
import pers.jay.library.network.BaseResponse

class WanResponse<T> : BaseResponse<T>() {

    // Gson解析用法
//    @SerializedName("errorCode")
//    override val code: Int = -999
//    @SerializedName("errorMsg")
//    override val msg: String? = null
//    @SerializedName("data")
//    override val data: T? = null

    // moshi解析用法
    @Json(name = "errorCode")
    override var code: Int = -999
    @Json(name = "errorMsg")
    override var msg: String? = null

    override val isSuccessful: Boolean
        get() = code == 0

    override fun toString(): String {
        return "BaseResponse(code=$code, errorCode=$code, data=${data}, msg=$msg, dataState=$dataState, errorReason=$errorReason)"
    }


}