package pers.jay.library.base.ext

import android.content.Context
import android.content.Intent
import android.view.View
import com.blankj.utilcode.util.ToastUtils

/**
 * 一些扩展方法
 */
fun showToast(message: String) = ToastUtils.showShort(message)

fun showToast(format: String, args: Any?) = ToastUtils.showShort(format, args)

fun showLongToast(message: String) = ToastUtils.showLong(message)

/**
 * inline（内联函数）： Kotlin编译器将内联函数的字节码插入到每一次调用方法的地方
 * reified（实化类型参数）： 在插入的字节码中，使用类型实参的确切类型代替类型实参
 * 注意：无法从 Java 代码里调用带实化类型参数的内联函数
 * @param map Intent携带参数
 */
inline fun <reified T> Context.startActivity(putExtraBlock: Intent.() -> Unit) {
    Intent(this, T::class.java).apply {
        putExtraBlock.invoke(this)
        startActivity(this)
    }
}

/**
 * 防重复点击
 * @param maxGap 最大间隔时间，单位：毫秒
 * @param onClick 点击事件回调
 */
fun View.singleClick(maxGap: Long = 1500L, onClick: () -> Unit) {
    var lastClick = 0L
    setOnClickListener {
        val gap = System.currentTimeMillis() - lastClick
        if (gap <= maxGap) {
            return@setOnClickListener
        }
        lastClick = System.currentTimeMillis()
        onClick.invoke()
    }
}