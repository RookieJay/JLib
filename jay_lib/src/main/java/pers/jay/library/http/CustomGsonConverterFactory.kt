package pers.jay.library.http

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.reflect.Type
import java.nio.charset.Charset
import kotlin.text.Charsets.UTF_8

/**
 * 自定义Gson转换工厂
 */
class CustomGsonConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type?,
        annotations: Array<Annotation>?,
        retrofit: Retrofit?
    ): CustomGsonResponseBodyConverter<out Any> {
        val adapter = gson.getAdapter(TypeToken.get(type!!))
        return CustomGsonResponseBodyConverter(gson, adapter as TypeAdapter<*>)
    }

    override fun requestBodyConverter(
        type: Type?,
        parameterAnnotations: Array<Annotation>?,
        methodAnnotations: Array<Annotation>?,
        retrofit: Retrofit?
    ): Converter<*, RequestBody>? {
        val adapter = gson.getAdapter(TypeToken.get(type!!))
        return CustomGsonRequestBodyConverter(gson, adapter as TypeAdapter<*>)
    }

    companion object {

        @JvmOverloads
        fun create(gson: Gson = Gson()): CustomGsonConverterFactory {
            return CustomGsonConverterFactory(gson)
        }
    }

    /**
     * 请求体转换器
     */
    class CustomGsonRequestBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<T, RequestBody> {

        @Throws(IOException::class)
        override fun convert(value: T): RequestBody {
            val buffer = Buffer()
            val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
            val jsonWriter = gson.newJsonWriter(writer)
            adapter.write(jsonWriter, value)
            jsonWriter.close()
            return buffer.readByteString().toRequestBody(MEDIA_TYPE)
        }

        companion object {
            private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaTypeOrNull()
            private val UTF_8 = Charset.forName("UTF-8")
        }
    }

    /**
     * 响应体转换器
     */
    class CustomGsonResponseBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): T {
            val body = value.string()
            val response = gson.fromJson(body, BaseResponse::class.java)
            //验证status返回是否正常
            if (response.code != HttpConst.HttpStatus_200) {
                value.close()
                // 响应数据不正确，抛出自定义异常
                throw CustomServerException(response.code, response.msg)
            }

            //继续处理body数据反序列化，注意value.string() 不可重复使用
            val contentType = value.contentType()
            val charset = if (contentType != null) contentType.charset(UTF_8) else UTF_8
            val inputStream = ByteArrayInputStream(body.toByteArray())
            val reader = InputStreamReader(inputStream, charset!!)
            val jsonReader = gson.newJsonReader(reader)

            value.use {
                return adapter.read(jsonReader)
            }
        }
    }
}