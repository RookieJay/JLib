package pers.jay.library.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * OkHttp的基类拦截器，根据需求重写方法确定是否拦截
 */
open class BaseInterceptor : Interceptor {

    companion object {
        protected val TAG: String = BaseInterceptor::class.java.simpleName
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return doIntercept(chain, request)
    }

    /**
     * 执行拦截具体操作
     * @param request 原始请求
     */
    protected open fun doIntercept(chain: Interceptor.Chain, request: Request): Response {
        return chain.proceed(request)
    }
}