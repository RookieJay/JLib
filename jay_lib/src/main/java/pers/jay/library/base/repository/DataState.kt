package pers.jay.library.base.repository

/**
 * @Author RookieJay
 * @Time 2021/5/27 13:39
 * @Description 数据状态枚举类
 */
enum class DataState(private val state: String) {

    STATE_CREATE("创建"), // 创建
    STATE_LOADING("加载中"), // 加载中
    STATE_SUCCESS("成功"), // 成功
    STATE_COMPLETED("完成"), // 完成
    STATE_EMPTY("数据为空"), // 数据为null
    STATE_FAILED("接口请求成功但是服务器返回error"), // 接口请求成功但是服务器返回error
    STATE_ERROR("请求失败"), // 请求失败
    STATE_UNKNOWN("未知原因"); // 未知

    fun getReason(): String {
        return state
    }
}