package pers.jay.library.network.errorhandle

/**
 * 自定义服务器异常
 */
class CustomServerException(val errorCode: Int, private val errorMessage: String?): RuntimeException(errorMessage)