package pers.jay.library.network.rxjava

/**
 * @Author RookieJay
 * @Time 2021/5/20 9:37
 * @description 网络请求失败原因
 */
enum class NetErrorReason {

    /**
     * 解析数据失败
     */
    PARSE_ERROR,

    /**
     * 网络问题
     */
    BAD_NETWORK,

    /**
     * 连接错误
     */
    CONNECT_ERROR,

    /**
     * 连接超时
     */
    CONNECT_TIMEOUT,

    /**
     * 未知错误
     */
    UNKNOWN_ERROR
}