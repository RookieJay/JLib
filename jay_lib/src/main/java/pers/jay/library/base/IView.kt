package pers.jay.library.base

import android.os.Bundle

interface IView {

    /**
     * 初始化各种View
     */
    fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化布局数据
     */
    fun initData(savedInstanceState: Bundle?)

    /**
     * 展示加载
     */
    fun showLoading() {}

    /**
     * 隐藏加载
     */
    fun hideLoading() {}

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