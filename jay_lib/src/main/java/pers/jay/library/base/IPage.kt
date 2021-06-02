package pers.jay.library.base

import android.content.Context
import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils

/**
 * @Author RookieJay
 * @Time 2021/5/21 16:26
 * @Description 所有页面的顶层接口，包括Activity、Fragment等
 */
interface IPage : IView {

    /**
     * 初始化页面各种View
     */
    fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化布局数据
     */
    fun initData(savedInstanceState: Bundle?)

    /**
     * 显示信息，默认展示toast
     */
    fun showMessage(message: String) {
        ToastUtils.showShort(message)
    }

    /**
     * 获取当前页面上下文
     */
    fun getContext(): Context

}