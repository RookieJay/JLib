package pers.jay.library.http

import com.blankj.utilcode.util.LogUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


abstract class BaseInterceptor : Interceptor {

    protected val TAG = javaClass.simpleName

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
//        LogUtils.dTag(TAG, "intercept: ${request.url.toString()}")
        if (needIntercept(request)) {
            return doIntercept(chain, request)
        }
        return chain.proceed(request)
    }

    /**
     * 满足拦截条件
     * @param request 原始请求
     * @return 是否拦截
     */
    protected abstract fun needIntercept(request: Request): Boolean

    /**
     * 执行拦截具体操作
     * @param request 原始请求
     */
    protected abstract fun doIntercept(chain: Interceptor.Chain, request: Request): Response
}