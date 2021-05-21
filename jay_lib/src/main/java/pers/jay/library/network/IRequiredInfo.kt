package pers.jay.library.network

import android.content.Context

/**
 * @Author RookieJay
 * @Time 2021/5/19 21:02
 * @description 依赖倒置：依赖接口不依赖实现 让其他依赖network层的东西实现此接口
 * 还应该遵循接口隔离原则，为每一层单独抽离接口
 */
interface IRequiredInfo {

    fun getAppVersion()

    fun getAppContext() : Context
}