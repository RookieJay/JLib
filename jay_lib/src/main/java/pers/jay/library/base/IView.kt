package pers.jay.library.base

import android.util.Log
import android.view.View

/**
 * @Author RookieJay
 * @Time 2021/5/21 16:26
 * @Description 所有view的顶层接口
 */
interface IView {

    /**
     * 展示加载
     */
    fun showLoading() {}

    /**
     * 隐藏加载
     */
    fun hideLoading() {}

    /**
     * 显示成功
     */
    fun showSuccess() {}

    /**
     * 显示空视图
     */
    fun showEmpty() {}

    /**
     * 展示错误
     */
    fun showError(message: String? = null) {
        message?.let { Log.e(javaClass.simpleName, "showError:$it") }
    }

    /**
     * 展示无网络
     */
    fun showNoNetwork() {}

    /**
     * 重试
     */
    fun onReload(view: View) {}

    /**
     * 杀死自己
     */
    fun exit() {}
}