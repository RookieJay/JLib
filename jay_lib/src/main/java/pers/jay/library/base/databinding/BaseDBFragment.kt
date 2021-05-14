package pers.jay.library.base.databinding

import android.os.Bundle
import android.util.Log
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import pers.jay.library.base.BaseFragment

/**
 * 基于DataBinding的Fragment基类
 */
abstract class BaseDBFragment<DB : ViewDataBinding> : BaseFragment() {

    protected lateinit var mBinding : DB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        beforeInit()
        initRootView(inflater, container)
        initView(savedInstanceState)
        Log.d(TAG, "onCreateView initData")
        initData(savedInstanceState)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * 初始化之前的逻辑
     */
    open fun beforeInit() {

    }

    private fun initRootView(inflater: LayoutInflater, container: ViewGroup?) {
        Log.d(TAG, "initRootView")
        val layoutId = initLayout()
        if (layoutId == 0) {
            throw InflateException("can not initialize layout")
        }
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        createBinding(inflater, container)
    }

    /**
     * 创建其他view的dataBinding
     */
    open fun createBinding(inflater: LayoutInflater, container: ViewGroup?) {
    }

    protected abstract fun initLayout(): Int

}