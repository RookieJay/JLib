package pers.jay.library.network.errorhandle

import android.util.Log
import androidx.annotation.StringRes
import pers.jay.library.R
import pers.jay.library.app.BaseApplication
import pers.jay.library.network.rxjava.NetErrorReason


object NetErrorHandler {

    private val TAG = NetErrorHandler::class.java.simpleName


    /**
     * 统一处理异常
     */
    fun handleException(reason: NetErrorReason) : String {
        Log.e(TAG, "handleException, $reason")
        return when (reason) {
            NetErrorReason.BAD_NETWORK -> getStringRes(R.string.NET_ERROR_MSG_BAD_NETWORK)
            NetErrorReason.PARSE_ERROR -> getStringRes(R.string.NET_ERROR_MSG_PARSE_ERROR)
            NetErrorReason.CONNECT_ERROR -> getStringRes(R.string.NET_ERROR_MSG_CONNECT_ERROR)
            NetErrorReason.CONNECT_TIMEOUT -> getStringRes(R.string.NET_ERROR_MSG_CONNECT_TIMEOUT)
            NetErrorReason.UNKNOWN_ERROR -> getStringRes(R.string.NET_ERROR_MSG_UNKNOWN_ERROR)
        }
    }

    private fun getStringRes(@StringRes resId: Int) = BaseApplication.instance().getString(resId)
}