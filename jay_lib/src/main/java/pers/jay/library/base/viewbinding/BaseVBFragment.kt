package pers.jay.library.base.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import pers.jay.library.base.BaseFragment
import pers.jay.library.utils.ViewBindingUtils

/**
 * 基于ViewBinding的Fragment基类
 *
 * ViewBinding的创建方式可选：
 * ①使用反射获取(默认)，但前提是当前子类的泛型严格限制位置和类型，即第一个泛型参数必须是ViewBinding类型
 * ②常规方式获取，重写useVBReflect()方法返回false,并重写initRootViewCommon()方法自行创建ViewBinding并返回
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseVBFragment<VB : ViewBinding> : BaseFragment(), IViewBinding<VB> {

    private var _binding: VB? = null
    protected val mBinding: VB
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initRootView(container)
    }

    private fun initRootView(container: ViewGroup?): View {
        _binding = if (useVBReflect()) {
            initRootViewByReflect(container)
        } else {
            initRootViewCommon(container)
        }
        return _binding!!.root
    }

    override fun initRootViewByReflect(container: ViewGroup?): VB {
        super.initRootViewByReflect(container)
        val vbClass = ViewBindingUtils.getVBClass(javaClass)
        vbClass?.apply {
            _binding = createViewBindingByReflect(vbClass as Class<VB>, container)
            return _binding!!
        }
        throw RuntimeException("can't find matched ViewBinding")
    }

    private fun createViewBindingByReflect(aClass: Class<VB>, container: ViewGroup?): VB {
        val method = aClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        return method.invoke(null, layoutInflater, container, false) as VB
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        beforeInit()
        initView(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 此处置空原因
        // Activity 实例和 Activity 视图的生命周期是同步的，而 Fragment 实例和 Fragment 视图的生命周期并不是完全同步的，
        // 因此需要在 Fragment 视图销毁时，手动回收绑定类对象，否则造成内存泄露。
        // 例如：detach Fragment，或者 remove Fragment 并且事务进入返回栈，此时 Fragment  视图销毁但 Fragment 实例存在。
        _binding = null
    }

    open fun beforeInit() {

    }

    /**
     * 默认使用反射初始化布局
     * 若想使用常规方式请重写此方法并返回false
     */
    override fun useVBReflect(): Boolean {
        return true
    }
}