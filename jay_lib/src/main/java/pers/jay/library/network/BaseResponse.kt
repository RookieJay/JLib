package pers.jay.library.network

/**
 * @Author RookieJay
 * @Time 2021/5/27 13:37
 * @Description 统一返回数据封装实体, 按需对各open属性进行重写
 */
open class BaseResponse<T> {

    open var code: Int = -999
    open var msg: String? = null
    open var data: T? = null

    // 数据状态
    var dataState: DataState? = DataState.CREATE
    // 错误原因
    var errorReason: String? = null

    // 响应数据是否正确，根据业务自行重写
    open fun isSuccessful() = code == 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseResponse<*>) return false
        if (code != other.code) return false
        if (msg != other.msg) return false
        if (data != other.data) return false
        if (dataState != other.dataState) return false
        if (errorReason != other.errorReason) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code
        result = 31 * result + (msg?.hashCode() ?: 0)
        result = 31 * result + (data?.hashCode() ?: 0)
        result = 31 * result + (dataState?.hashCode() ?: 0)
        result = 31 * result + (errorReason?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "${this::class.simpleName}(code=$code, msg=$msg, data=$data, dataState=$dataState, errorReason=$errorReason)"
    }

    /**
     * @Author RookieJay
     * @Time 2021/5/27 13:39
     * @Description 数据状态枚举类
     */
    enum class DataState(private val state: String) {

        CREATE("创建"), // 创建
        LOADING("加载中"), // 加载中
        SUCCESS("成功"), // 成功
        DATA_RESULT("返回数据"), // 返回数据
//        COMPLETED("完成"), // 完成
        EMPTY("数据为空"), // 数据为null
        BUSS_ERROR("业务异常"), // 接口请求成功但是服务器返回error
        REQUEST_ERROR("请求失败"), // 请求失败
        UNKNOWN("未知异常"); // 未知

        fun value(): String {
            return state
        }
    }

}