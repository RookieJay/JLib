package pers.jay.library.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.LogUtils
import java.lang.reflect.ParameterizedType

/**
 * 基于ViewBinding的Fragment基类
 *
 * ViewBinding的创建方式可选：
 * ①使用反射获取(默认)，但前提是当前子类的泛型严格限制位置和类型，即第一个泛型参数必须是ViewBinding类型
 * ②常规方式获取，重写userReflect()方法返回false,并重写initViewBinding()方法自行创建ViewBinding并返回
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseVBFragment<VB : ViewBinding> : BaseFragment() {

    protected lateinit var mBinding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initRootView(container)
    }

    private fun initRootView(container: ViewGroup?): View? {
        mBinding = if (useVBReflect()) {
            recursiveFindViewBinding(javaClass, container)
        } else {
            initViewBinding(container)
        }
        return mBinding.root
    }

    /**
     * 常规方式获取ViewBinding，重写此方法自行创建，
     * fixme 默认还是使用反射根据当前方法返回类型获取，在方法内部获取自身的返回类型有问题。
     */
    open fun initViewBinding(container: ViewGroup?): VB {
//        LogUtils.d("当前实现类:${javaClass.simpleName}")
//        for (method in javaClass.methods) {
//            LogUtils.d("遍历method： ${method.name}")
//        }
//
//        val method = javaClass.getDeclaredMethod("initViewBinding", ViewGroup::class.java)
//        val returnType = method.returnType
//        val inflateMethod = returnType.getDeclaredMethod(
//            "inflate",
//            LayoutInflater::class.java,
//            ViewGroup::class.java,
//            Boolean::class.java
//        )
//        mBinding = inflateMethod.invoke(null, layoutInflater, container, false) as VB
        return mBinding
    }

    /**
     * 递归查找当前fragment->父类的泛型参数,当前类没有泛型，则向上查找父类，直到找到为止
     */
    private fun recursiveFindViewBinding(
        fragmentClazz: Class<*>,
        container: ViewGroup?
    ): VB {
        val type = fragmentClazz.genericSuperclass
        return if (type is ParameterizedType) {
            val aClass = type.actualTypeArguments[0] as Class<VB>
            LogUtils.d("aClass: ${aClass.simpleName}")
            createViewBinding(aClass, container)
        } else {
            val clazz = fragmentClazz.superclass as Class<*>
            recursiveFindViewBinding(clazz, container)
        }
    }

    private fun createViewBinding(aClass: Class<VB>, container: ViewGroup?): VB {
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

    abstract fun beforeInit()

    open fun useVBReflect(): Boolean {
        return true
    }
}