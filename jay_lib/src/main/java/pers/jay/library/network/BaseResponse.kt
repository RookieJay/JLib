package pers.jay.library.network
/**
 * @class describe
 * @author ${USER}
 * @time ${DATE} ${TIME}
 * @change
 * @chang time
 * @class describe
 */
data class BaseResponse<out T>(val code: Int, val msg: String, val data: T)