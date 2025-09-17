package pers.jay.library.network

import android.text.TextUtils
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.Objects
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * @Author RookieJay
 * @Time 2021/5/19 16:00
 * @Description 描述：网络管理基类，用于创建及配置okHttp及Retrofit
 *
 */
@Suppress("UNCHECKED_CAST")
abstract class NetworkManager {

    protected val mOkHttpClient: OkHttpClient by lazy {
        createOkHttpClient()
    }

    protected val mRetrofit: Retrofit by lazy {
        getRetrofit()
    }

    protected val mServiceMap by lazy {
        ConcurrentHashMap<Class<*>, Any>()
    }

    private fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .readTimeout(HttpConfig.DEFAULT_READ_WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
            .writeTimeout(HttpConfig.DEFAULT_READ_WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
            .connectTimeout(HttpConfig.DEFAULT_CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
        if (needPrintNetworkLog()) {
            builder.addNetworkInterceptor(LoggingInterceptor.Builder().build())
        }
        applyOkHttpClientConfig(builder)
        return builder.build()
    }

    private fun getRetrofit(): Retrofit {
        val baseUrl = getBaseUrl()
        if (TextUtils.isEmpty(baseUrl)) {
            throw NullPointerException("baseUrl can not be empty")
        }
        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
        if (useGsonConverter()) {
            builder.addConverterFactory(CustomGsonConverterFactory.create())
        }
        if (useScalarsConverter()) {
            // to convert strings and both primitives and their boxed types to {@code text/plain} bodies.
            builder.addConverterFactory(ScalarsConverterFactory.create())
        }
        applyRetrofitConfig(builder)
        return builder.client(mOkHttpClient).build()
    }

    /**
     * 获取apiService，使用缓存获取单例
     * @param  apiClass 具体apiService的泛型类型
     * @return 由Retrofit生成的service实例
     */
    fun <T> getApiService(apiClass: Class<T>): T {
        Objects.requireNonNull(apiClass, "api service class can not be null")
        val api = mServiceMap[apiClass]
        return if (api != null) {
            api as T
        } else {
            val t = mRetrofit.create(apiClass)
            mServiceMap[apiClass] = t!!
            t
        }
    }

    /**
     * 获取baseUrl
     * @return 设置到retrofit的baseUrl
     */
    abstract fun getBaseUrl(): String

    /**
     * 是否需要打印网络日志，默认为true
     */
    open fun needPrintNetworkLog() = true

    /**
     * 是否使用Gson转换器，默认为true
     */
    open fun useGsonConverter() = true

    /**
     * 是否使用Scalars转换器，默认为true
     */
    open fun useScalarsConverter() = true

    /**
     * 应用OkHttpClient配置，下层实现，可对现有okHttpClient进行定制，如添加拦截器xxInterceptor
     * @param  builder OkHttp构建器
     * @return Unit
     */
    open fun applyOkHttpClientConfig(builder: OkHttpClient.Builder) {}

    /**
     * 应用Retrofit配置，下层实现，可对现有Retrofit进行定制，如添加转换器xxConverter
     * @param  builder Retrofit构建器
     * @return Unit
     */
    open fun applyRetrofitConfig(builder: Retrofit.Builder) {}

}