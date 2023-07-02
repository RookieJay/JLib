package pers.jay.library.base.ext

import android.content.res.Resources
import android.util.TypedValue

/**
 * 单位转换扩展 px单位与其他互转
 * 类型详见[TypedValue]
 */

val Float.dp
    get() = dp2px(this)

fun dp2px(dpValue: Float): Float {
    return convert2px(dpValue, TypedValue.COMPLEX_UNIT_DIP)
}

fun sp2px(dpValue: Float): Float {
    return convert2px(dpValue, TypedValue.COMPLEX_UNIT_SP)
}

fun px2dp(pxValue: Float): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

fun px2sp(pxValue: Float): Int {
    val scale = Resources.getSystem().displayMetrics.scaledDensity
    return (pxValue / scale + 0.5f).toInt()
}

fun convert2px(originValue: Float, unit: Int): Float {
    return TypedValue.applyDimension(
        unit,
        originValue,
        Resources.getSystem().displayMetrics
    )
}