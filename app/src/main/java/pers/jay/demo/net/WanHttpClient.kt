package pers.jay.demo.net

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import pers.jay.library.network.NetworkManager
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * @Author RookieJay
 * @Time 2021/5/19 16:06
 * @Description
 */
object WanHttpClient: NetworkManager() {

    override fun getBaseUrl(): String {
        return "https://www.wanandroid.com/"
    }

    fun getWanService() = getApiService(WanService::class.java)

    override fun useGsonConverter(): Boolean {
        return false
    }

    override fun applyRetrofitConfig(builder: Retrofit.Builder) {
        super.applyRetrofitConfig(builder)

        /**
         * 使用moshi进行json解析，
         * 已知使用条件：KotlinJsonAdapterFactory+@JsonClass
         * 注意事项：经试验，当属性与json字段名称不一致时，使用@Json注解，但属性声明必须是var才会生效.
         * 示例见[WanResponse]
         */

        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        builder.addConverterFactory(MoshiConverterFactory.create(moshi))
    }
}