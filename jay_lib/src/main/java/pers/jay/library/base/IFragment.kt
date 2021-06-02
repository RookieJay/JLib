package pers.jay.library.base

import android.os.Bundle

/**
 * @Author RookieJay
 * @Time 2021/5/21 18:21
 * @Description Fragment顶层接口，所有Fragment实现此接口，以满足规范
 */
interface IFragment : IPage {

    /**
     * 初始化页面参数
     */
    fun initParams(bundle: Bundle?) {}
}