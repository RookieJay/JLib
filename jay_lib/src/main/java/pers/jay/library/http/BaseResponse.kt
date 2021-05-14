package pers.jay.library.http

data class BaseResponse<out T>(val code: Int, val msg: String, val data: T)