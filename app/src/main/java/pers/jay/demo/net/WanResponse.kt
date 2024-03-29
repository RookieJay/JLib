package pers.jay.demo.net

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import pers.jay.library.network.BaseResponse

@JsonClass(generateAdapter = true)
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
    @Json(name = "data")
    override var data: T? = null

    override fun isSuccessful() = code == 0

}