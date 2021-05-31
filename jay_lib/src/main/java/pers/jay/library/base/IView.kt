package pers.jay.library.base

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
     * 展示错误
     */
    fun showError(message: String? = null) {}

    /**
     * 展示无网络
     */
    fun showNoNetwork() {}

    /**
     * 杀死自己
     */
    fun killSelf() {}

}