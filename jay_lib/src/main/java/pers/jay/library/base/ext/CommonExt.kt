package pers.jay.library.base.ext

import com.blankj.utilcode.util.ToastUtils

fun showToast(message: String) = ToastUtils.showShort(message)

fun showToast(format: String, args: Any?) = ToastUtils.showShort(format, args)

fun showLongToast(message: String) = ToastUtils.showLong(message)
