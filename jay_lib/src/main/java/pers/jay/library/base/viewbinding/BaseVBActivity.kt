package pers.jay.library.base.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import pers.jay.library.base.BaseActivity
import java.lang.reflect.ParameterizedType

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
        initView(savedInstanceState)
        initData(savedInstanceState)
    }

    /**
     *  反射，调用特定ViewBinding中的inflate方法填充视图
     */
    override fun initRootViewByReflect(container: ViewGroup?): VB? {
        val type = javaClass.genericSuperclass
        // ParameterizedType 参数化类型 声明类型中带有“<>”的都是参数化类型
        if (type is ParameterizedType) {
            val clazz = type.actualTypeArguments[0] as Class<VB>
            val method = clazz.getMethod("inflate", LayoutInflater::class.java)
            mBinding = method.invoke(null, layoutInflater) as VB
            setContentView(mBinding.root)
        }
        return mBinding
    }

    /**
     * 默认使用反射初始化布局
     * 若想使用常规方式请重写此方法并返回false
     */
    override fun useVBReflect(): Boolean {
        return true
    }


}