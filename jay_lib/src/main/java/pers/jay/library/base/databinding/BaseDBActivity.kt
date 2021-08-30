package pers.jay.library.base.databinding

import android.os.Bundle
import android.view.InflateException
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import pers.jay.library.base.BaseActivity

/**
 * 基于DataBinding的基类Activity
 */
abstract class BaseDBActivity<DB : ViewDataBinding> : BaseActivity() {

    protected lateinit var mBinding : DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeInit()
        initRootView(savedInstanceState)
        initLoadSir()
        initView(savedInstanceState)
        initData(savedInstanceState)
    }

    /**
     * 初始化根布局
     */
    private fun initRootView(savedInstanceState: Bundle?) {
        val layoutResId = initLayout(savedInstanceState)
        try {
            if (layoutResId != 0) {
                mBinding = DataBindingUtil.setContentView(this, layoutResId)
            }
        } catch (e: Exception) {
            if (e is InflateException) throw e
            e.printStackTrace()
        }
    }

    /**
     * 初始化之前的逻辑
     */
    open fun beforeInit() {

    }

    /**
     * 初始化布局layoutId,如果 {@link #initView(Bundle)} 返回 0, 框架则不会调用 {@link Activity#setContentView(int)}
     */
    abstract fun initLayout(savedInstanceState: Bundle?) : Int

    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }

}