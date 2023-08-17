package pers.jay.library.network.mutiDomain

import android.text.TextUtils
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import pers.jay.library.network.BaseInterceptor
import pers.jay.library.network.mutiDomain.MultiDomain.API_DOMAIN_HEADER_KEY

/**
 * 动态域名拦截器
 * 可以在不实例化多个Retrofit的情况下，通过在@Header注解中传入参数，实现动态切换请求域名而无需实例化多个Retrofit
 *
 * 使用示例：
 *
 *  在给retrofit设置了baseUrl的情况下，使用
 *
 *  @Headers("${MultiDomain.API_DOMAIN_HEADER_KEY}: ${Const.HWeather.API_GEO}")
 *  @GET("/v2/test/lookup/")
 *  suspend fun request(): Response
 *
 *  即可将baseUrl替换为Const.HWeather.API_GEO的值
 */
class DynamicDomainInterceptor: BaseInterceptor() {

    companion object {
        private val TAG = DynamicDomainInterceptor::class.simpleName
    }

    override fun doIntercept(chain: Interceptor.Chain, request: Request): Response {
        if (!MultiDomain.enable) {
            return doNoting(chain, request)
        }
        // 根据@Headers注解声明的header的key获取header中的value,Headers允许一对多，即一个key可以存在多个Value
        // 如声明@Headers("Accept: application/json")，则调用request.headers("Accept")获取到的的值为"application/json"
        val allHeaders = request.headers
        Log.i(TAG, "allHeaders:$allHeaders")
        val domains = request.headers(API_DOMAIN_HEADER_KEY)
        if (domains.isEmpty()) {
            return doNoting(chain, request)
        }

        val targetDomainUrl = request.header(API_DOMAIN_HEADER_KEY)
        if (targetDomainUrl.isNullOrEmpty()) {
            return doNoting(chain, request)
        }
        val httpUrl = request.url.toUrl()
        MultiDomain.baseUrl = httpUrl.protocol + "://" + httpUrl.host
        Log.i(TAG, "baseUrl=${MultiDomain.baseUrl}")
        if (TextUtils.equals(MultiDomain.baseUrl, targetDomainUrl)) {
            Log.w(TAG, "the targetDomainUrl is equals to baseUrl")
            return doNoting(chain, request)
        }
        val originUrl = request.url.toString()
        Log.i(TAG, "originUrl:$originUrl, domainUrl:$targetDomainUrl")
        val newRequestUrl = originUrl.replace(MultiDomain.baseUrl, targetDomainUrl)
        Log.i(TAG, "replace url to: $newRequestUrl")
        val newRequest = request.newBuilder().url(newRequestUrl).build()
        return chain.proceed(newRequest)
    }

    private fun doNoting(chain: Interceptor.Chain, request: Request) = chain.proceed(request)

}