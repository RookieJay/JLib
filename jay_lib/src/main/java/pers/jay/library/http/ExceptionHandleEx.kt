package pers.jay.library.http

import android.util.Log
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.EOFException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * 统一异常处理(网络,接口错误…)
 *
 * 对Retrofit的Call<T>类进行扩展，捕捉异常进行处理
 */

const val TAG = "Net Error"

fun Exception.errorHandle(e: Exception, customErrorHandle: (code: Int, msg: String?) -> Unit) {
    var code = 0
    var message = "未知错误"
    when (e) {
        is CustomServerException -> {
            code = e.errorCode
            message = e.message.toString()
        }
        is HttpException -> {
            code = e.code()
            message = e.message.toString()
        }
        is UnknownHostException -> {
            code = 400
            message = "无法连接到服务器:未知主机"
        }
        is SocketTimeoutException -> {
            code = 400
            message = "连接服务器超时"
        }
        is ConnectException -> {
            code = 400
            message = "连接到服务器失败"
        }
        is SocketException -> {
            code = 400
            message = "链接关闭"
        }
        is EOFException -> {
            code = 400
            message = "链接关闭"
        }
        is IllegalArgumentException -> {
            code = 400
            message = "参数错误"
        }
        is SSLException -> {
            code = 400
            message = "证书错误"
        }
        is NullPointerException -> {
            message = "数据为空"
        }
        is JsonSyntaxException -> {
            message = "数据解析异常"
        }
    }
    defaultError(code, message)
    Log.e("$TAG 原始错误信息", e.toString())
    e.printStackTrace()
    customErrorHandle(code, message)
}

private val defaultError = fun(code: Int, msg: String?) {
    val displayMsg = String.format("%s(%s)", msg, code)
    Log.e(TAG, displayMsg)
    ToastUtils.showShort(msg)
}