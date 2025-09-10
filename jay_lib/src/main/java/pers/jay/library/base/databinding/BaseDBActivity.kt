package pers.jay.library.base.databinding

import android.view.InflateException
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import pers.jay.library.base.BaseActivity

/**
 * 基于DataBinding的基类Activity
 */
@Deprecated("@see [BaseBindingActivity]")
abstract class BaseDBActivity<DB : ViewDataBinding> : BaseActivity() {

    protected lateinit var mBinding : DB

    override fun getContentView(): View {
        val layoutResId = initLayout()
        try {
            if (layoutResId != 0) {
                mBinding = DataBindingUtil.setContentView(this, layoutResId)
            }
        } catch (e: Exception) {
            if (e is InflateException) throw e
            e.printStackTrace()
        }
        return mBinding.root
    }

    /**
     * 初始化布局layoutId,如果 {@link #initView(Bundle)} 返回 0, 框架则不会调用 {@link Activity#setContentView(int)}
     */
    abstract fun initLayout() : Int

    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }

}