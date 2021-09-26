package pers.jay.library.network.rxjava

import android.net.ParseException
import com.blankj.utilcode.util.NetworkUtils
import com.google.gson.JsonParseException
import io.reactivex.observers.ResourceObserver
import org.json.JSONException
import pers.jay.library.network.errorhandle.ErrorMessageParser
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * @Author RookieJay
 * @Time 2021/5/21 11:43
 * @Description 基于rxJava的通用网络请求观察者，自动进行网络异常处理并回调
 */
abstract class BaseNetObserver<T> : ResourceObserver<T>() {

    /**
     * @desc 事件开始
     */
    override fun onStart() {
        super.onStart()
        if (!NetworkUtils.isAvailable()) {
            handleException(NetErrorReason.BAD_NETWORK)
        }
    }

    /**
     * 请求成功，返回数据
     */
    override fun onNext(t: T) {
        if (!handleExceptionFromServer(t)) {
            onSuccess(t)
        }
    }

    /**
     * @desc   子类对服务端抛出的异常进行处理，默认不处理
     * @param  t 返回数据
     * @return 是否处理服务端异常
     */
    open fun handleExceptionFromServer(t: T) : Boolean = false

    /**
     * 请求失败，网络异常处理
     */
    override fun onError(exception: Throwable) {
        exception.printStackTrace()
        val errorMsg = when (exception) {
            is ConnectException, is UnknownHostException, is NoRouteToHostException -> {
                handleException(NetErrorReason.CONNECT_ERROR)
            }
            is InterruptedIOException, is SSLException -> {
                handleException(NetErrorReason.CONNECT_TIMEOUT)
            }
            is HttpException -> {
                handleException(NetErrorReason.CONNECT_ERROR)
            }
            is JsonParseException, is JSONException, is ParseException -> {
                handleException(NetErrorReason.PARSE_ERROR)
            }
            else -> {
                handleException(NetErrorReason.UNKNOWN_ERROR)
            }
        }
        onException(errorMsg)
    }

    private fun handleException(netErrorReason: NetErrorReason) = ErrorMessageParser.getErrorMessage(netErrorReason)

    /**
     * @desc 事件结束
     */
    override fun onComplete() {}

    /**
     * @desc   请求成功回调
     * @param  t 响应并解析成功的数据
     * @return Unit
     */
    abstract fun onSuccess(t: T)

    /**
     * @desc   请求异常回调
     * @param  errorMsg 错误信息
     * @return Unit
     */
    abstract fun onException(errorMsg: String)

}