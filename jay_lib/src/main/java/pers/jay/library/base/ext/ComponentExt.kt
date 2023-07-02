package pers.jay.library.base.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import java.io.Serializable

/**
 * 组件（Activity、Fragment等）功能扩展
 */


//inline fun <reified T> Context.startActivity(block: Intent.() -> Unit) {
//    val intent = Intent(this, T::class.java)
//
//    intent.block()
//    startActivity(intent)
//}

/**
 * inline（内联函数）： Kotlin编译器将内联函数的字节码插入到每一次调用方法的地方
 * reified（实化类型参数）： 在插入的字节码中，使用类型实参的确切类型代替类型实参
 *
 * 注意：无法从 Java 代码里调用带实化类型参数的内联函数
 *
 *
 *
 * @param block Intent扩展函数
 */
inline fun <reified T> Context.startActivity(vararg params: Pair<String, Any>, block: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java).apply {
        // 可变参数传递时在变量前面加*
        putExtras(*params)
        block()
    }
    startActivity(intent)
}

/**
 * Intent传参扩展，利用可变参数实现一个方法传入所有参数
 *
 * 示例：Intent().putExtras("id" to 1, "name" to "test")
 *
 * @param params 参数键值对
 */
fun Intent.putExtras(vararg params: Pair<String, Any>): Intent {
    if (params.isEmpty()) return this
    params.forEach { (key, value) ->
        when (value) {
            is Int -> putExtra(key, value)
            is Byte -> putExtra(key, value)
            is Char -> putExtra(key, value)
            is Long -> putExtra(key, value)
            is Float -> putExtra(key, value)
            is Short -> putExtra(key, value)
            is Double -> putExtra(key, value)
            is Boolean -> putExtra(key, value)
            is Bundle -> putExtra(key, value)
            is String -> putExtra(key, value)
            is IntArray -> putExtra(key, value)
            is ByteArray -> putExtra(key, value)
            is CharArray -> putExtra(key, value)
            is LongArray -> putExtra(key, value)
            is FloatArray -> putExtra(key, value)
            is Parcelable -> putExtra(key, value)
            is ShortArray -> putExtra(key, value)
            is DoubleArray -> putExtra(key, value)
            is BooleanArray -> putExtra(key, value)
            is CharSequence -> putExtra(key, value)
            is Array<*> -> {
                when {
                    value.isArrayOf<String>() ->
                        putExtra(key, value as Array<String?>)
                    value.isArrayOf<Parcelable>() ->
                        putExtra(key, value as Array<Parcelable?>)
                    value.isArrayOf<CharSequence>() ->
                        putExtra(key, value as Array<CharSequence?>)
                    else -> putExtra(key, value)
                }
            }
            is Serializable -> putExtra(key, value)
        }
    }
    return this
}

inline fun <reified T: Any> Activity.getParam(key: String, defaultValue: T? = null) = lazy {
    val value = intent.extras?.get(key)
    if (value is T) {
        value
    } else {
        defaultValue
    }
}

inline fun <reified T: Any> Fragment.getParam(key: String, defaultValue: T? = null) = lazy {
    val value = arguments?.get(key)
    if (value is T) value else defaultValue
}

fun Bundle.put(params: Array<out Pair<String, Any?>>): Bundle {
    params.forEach {
        val key = it.first
        val value = it.second
        when (value) {
            is Int -> putInt(key, value)
            is IntArray -> putIntArray(key, value)
            is Long -> putLong(key, value)
            is LongArray -> putLongArray(key, value)
            is CharSequence -> putCharSequence(key, value)
            is String -> putString(key, value)
            is Float -> putFloat(key, value)
            is FloatArray -> putFloatArray(key, value)
            is Double -> putDouble(key, value)
            is DoubleArray -> putDoubleArray(key, value)
            is Char -> putChar(key, value)
            is CharArray -> putCharArray(key, value)
            is Short -> putShort(key, value)
            is ShortArray -> putShortArray(key, value)
            is Boolean -> putBoolean(key, value)
            is BooleanArray -> putBooleanArray(key, value)
            is Serializable -> putSerializable(key, value)
            is Parcelable -> putParcelable(key, value)
            is Bundle -> putAll(value)
            is Array<*> -> when {
                value.isArrayOf<Parcelable>() -> putParcelableArray(key, value as Array<out Parcelable>?)
            }
        }
    }
    return this
}


