package pers.jay.library.base.databinding

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

    override fun initRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        Log.d(TAG, "initRootView")
        val layoutId = initLayout()
        if (layoutId == 0) {
            throw InflateException("can not initialize layout")
        }
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return mBinding.root
    }
    protected abstract fun initLayout(): Int

}