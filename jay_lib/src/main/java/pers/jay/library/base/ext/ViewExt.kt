package pers.jay.library.base.ext

import android.view.View

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
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
