package pers.jay.library.base.ext

import android.app.Activity
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

/**
 * Ui功能扩展，如：获取颜色等
 *
 */


fun View.getColor(@ColorRes resId: Int) = ContextCompat.getColor(context, resId)

fun View.getDrawable(@DrawableRes resId: Int) = ContextCompat.getDrawable(context, resId)

fun Activity.getColor(@ColorRes resId: Int) =  ContextCompat.getColor(this, resId)

fun Activity.getDrawable(@DrawableRes resId: Int) = ContextCompat.getDrawable(this, resId)

fun Fragment.getColor(@ColorRes resId: Int) = ContextCompat.getColor(requireContext(), resId)

fun Fragment.getDrawable(@DrawableRes resId: Int) = ContextCompat.getDrawable(requireContext(), resId)


