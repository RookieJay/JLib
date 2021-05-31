package pers.jay.library.base

/**
 * @Author RookieJay
 * @Time 2021/5/21 16:08
 * @Description 状态视图接口，界面状态事件回调
 */
interface IViewStatus {

    /**
     * 加载更多
     */
    fun loadMore() {}

    /**
     * 结束加载更多
     */
    fun finishLoadMore() {}

    /**
     * 展示空视图
     */
    fun showEmptyView() {}

    /**
     * 展示错误视图
     */
    fun showErrorView() {}
}