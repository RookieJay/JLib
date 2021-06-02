package pers.jay.library.base

import android.content.Intent

/**
 * @Author RookieJay
 * @Time 2021/5/21 18:21
 * @Description Activity顶层接口，所有Activity实现此接口，以满足规范
 */
interface IActivity : IPage {

    /**
     * 初始化页面参数
     */
    fun initParams(intent: Intent) {}

}