package pers.jay.library.utils

import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

object ViewBindingUtils {

    /**
     * 从给定类的泛型参数中找到ViewBinding类型
     */
    fun getInstancedGenericClass(from: Class<*>): Class<*>? {
        try {
            val type = from.genericSuperclass
            if (type is ParameterizedType) {
                val types = type.actualTypeArguments
                for (temp in types) {
                    if (temp is Class<*>) {
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
}