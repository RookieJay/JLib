package pers.jay.library.http

/**
 * 自定义服务器异常
 */
class CustomServerException(val errorCode: Int, val erroMessge: String): RuntimeException(erroMessge)