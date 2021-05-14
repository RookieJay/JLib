package pers.jay.library.http

import android.text.TextUtils
import android.util.Log
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 网络管理类
 */
abstract class NetworkManager {

    lateinit var mOkHttpClient: OkHttpClient
    lateinit var mRetrofit: Retrofit

    companion object {
        const val READ_WRITE_TIME_OUT = 5000L
        const val CONNECT_TIME_OUT = 3000L
    }

    init {
        createOkhttpClient()
        getRetrofit()
    }

    fun createOkhttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(LoggingInterceptor.Builder()
            .setLevel(Level.BASIC)
            .log(Log.VERBOSE)
            .build())
        addInterceptors(builder)
        mOkHttpClient = builder.readTimeout(READ_WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
            .writeTimeout(READ_WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
            .build()

        return mOkHttpClient
    }

    abstract fun addInterceptors(builder: OkHttpClient.Builder)

    fun getRetrofit(): Retrofit {
        val baseUrl = getBaseUrl()
        if (TextUtils.isEmpty(baseUrl)) {
            throw RuntimeException("baseUrl can not be empty")
        }
        mRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(CustomGsonConverterFactory.create()) // use Gson
            .addConverterFactory(ScalarsConverterFactory.create()) // to receive none-json response
            .client(mOkHttpClient)
            .build()
        return mRetrofit
    }

    fun <T> getApiService(tClass: Class<T>): T {
        Objects.requireNonNull(tClass, "api service class can not be null")
        return mRetrofit.create(tClass)
    }

    abstract fun getBaseUrl(): String

}