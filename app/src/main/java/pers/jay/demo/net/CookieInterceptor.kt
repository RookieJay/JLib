package pers.jay.demo.net

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import pers.jay.library.network.BaseInterceptor

class CookieInterceptor : BaseInterceptor() {

    override fun needIntercept(request: Request): Boolean {
       return true
    }

    override fun doIntercept(chain: Interceptor.Chain, request: Request): Response {
        return chain.proceed(request)
    }

}