package pers.jay.library.base.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import pers.jay.library.base.BaseActivity
import pers.jay.library.utils.ViewBindingUtils

/**
 * @Author RookieJay
 * @Time 2021/5/21 18:42
 * @Description 基于[ViewBinding]和[ViewModel]的Activity基类
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseVBActivity<VB : ViewBinding> : BaseActivity(), IViewBinding<VB> {

    /**
     * 页面布局绑定，所有视图从这里获取
     */
    protected lateinit var mBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeInit(savedInstanceState)
        init(savedInstanceState)
    }

    open fun beforeInit(savedInstanceState: Bundle?) {}

    private fun init(savedInstanceState: Bundle?) {
        if (useVBReflect()) {
            initRootViewByReflect()
        } else {
            initRootViewCommon()
        }
        initLoadSir()
        initView(savedInstanceState)
        initData(savedInstanceState)
    }

    /**
     *  反射，调用特定ViewBinding中的inflate方法填充视图
     */
    override fun initRootViewByReflect(container: ViewGroup?): VB? {
        val vbClass = ViewBindingUtils.getVBClass(javaClass)
        vbClass?.apply {
            val method = getMethod("inflate", LayoutInflater::class.java)
            mBinding = method.invoke(null, layoutInflater) as VB
            setContentView(mBinding.root)
            return mBinding
        }
        throw RuntimeException("can't find matched ViewBinding")
    }

    /**
     * 默认使用反射初始化布局
     * 若想使用常规方式请重写此方法并返回false
     */
    override fun useVBReflect(): Boolean {
        return true
    }


}