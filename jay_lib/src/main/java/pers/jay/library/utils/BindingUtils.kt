package pers.jay.library.utils

import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

object BindingUtils {

    private val TAG = "ViewBindingUtils"

    /**
     * 从给定类的泛型参数中找到ViewBinding/ViewDataBinding类型
     */
    fun getBindingClass(from: Class<*>): Class<*>? {
        try {
            val type = from.genericSuperclass
            if (type is ParameterizedType) {
                val types = type.actualTypeArguments
                for (temp in types) {
                    if (temp is Class<*>) {
                        if (isSupportedBindingType(temp)) {
                            return temp
                        }
                    } else if (temp is ParameterizedType) {
                        val rawType = temp.rawType
                        if (rawType is Class<*> && isSupportedBindingType(rawType)) {
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

    private fun isSupportedBindingType(clazz: Class<*>): Boolean {
        return ViewBinding::class.java.isAssignableFrom(clazz) ||
                ViewDataBinding::class.java.isAssignableFrom(clazz)
    }

}