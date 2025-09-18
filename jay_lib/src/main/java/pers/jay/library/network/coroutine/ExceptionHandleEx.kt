package pers.jay.library.network.coroutine

import android.net.ParseException
import com.google.gson.JsonParseException
import org.json.JSONException
import pers.jay.library.network.errorhandle.ErrorMessageParser
import pers.jay.library.network.rxjava.NetErrorReason
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * 统一异常处理(网络,接口错误…)
 *
 */

const val TAG = "Net Error"

fun Throwable.getRequestErrorMsg(): String {
    val errorReason : NetErrorReason = when (this) {
        is ConnectException, is UnknownHostException, is NoRouteToHostException -> {
            NetErrorReason.CONNECT_ERROR
        }
        is InterruptedIOException, is SSLException -> {
            NetErrorReason.CONNECT_TIMEOUT
        }
        is HttpException -> {
            NetErrorReason.BAD_NETWORK
        }
        is JsonParseException, is JSONException, is ParseException -> {
            NetErrorReason.PARSE_ERROR
        }
        is IllegalArgumentException -> {
            if (this.message?.contains("Unable to create converter for") == true) {
                NetErrorReason.PARSE_ERROR
            } else {
                NetErrorReason.UNKNOWN_ERROR
            }
        }
        else -> {
            NetErrorReason.UNKNOWN_ERROR
        }
    }
    return ErrorMessageParser.getErrorMessage(errorReason)
}
