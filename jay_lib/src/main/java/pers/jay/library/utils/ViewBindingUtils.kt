package pers.jay.library.utils

import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.LogUtils
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

object ViewBindingUtils {

    private val TAG = "ViewBindingUtils"

    /**
     * 从给定类的泛型参数中找到ViewBinding类型
     */
    fun getVBClass(from: Class<*>): Class<*>? {
        LogUtils.d(TAG, "getInstancedGenericClass:${from.simpleName}")
        try {
            // 返回当前对象所表示的类的超类，且包含该超类的泛型，如BActivity<BViewModel>
            val type = from.genericSuperclass
            // 判断是参数化类型（即泛型）
            if (type is ParameterizedType) {
                // 获取该类所有泛型类型组成的数组
                val types = type.actualTypeArguments
                for (temp in types) {
                    LogUtils.d(TAG, "temp in types:${temp}")
                    if (temp is Class<*>) {
                        LogUtils.d(TAG, "temp:${temp.simpleName}")
                        if (ViewBinding::class.java.isAssignableFrom(temp)) {
                            return temp
                        }
                    } else if (temp is ParameterizedType) {
                        val rawType = temp.rawType
                        if (rawType is Class<*> && ViewBinding::class.java.isAssignableFrom(rawType)) {
                            return rawType
                        }
                    }
                }
            }
        } catch (e: java.lang.reflect.GenericSignatureFormatError) {
            e.printStackTrace()
        } catch (e: TypeNotPresentException) {
            e.printStackTrace()
        } catch (e: java.lang.reflect.MalformedParameterizedTypeException) {
            e.printStackTrace()
        }
        return null
    }

    inline fun <reified T: Any> getInstancedGenericKClass(from : KClass<T>) {
        val TAG = "getInstancedGenericKClass"
        LogUtils.d(TAG, " from: ${from.simpleName}")
        val typeParameters = from.typeParameters
        LogUtils.d(TAG, "typeParameters: $typeParameters")
        for (typeParameter in typeParameters) {

        }

    }
}