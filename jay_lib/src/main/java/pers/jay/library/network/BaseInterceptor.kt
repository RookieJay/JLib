package pers.jay.library.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * OkHttp的基类拦截器，根据需求重写方法确定是否拦截
 */
abstract class BaseInterceptor : Interceptor {

    companion object {
        protected val TAG: String = BaseInterceptor::class.java.simpleName
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
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