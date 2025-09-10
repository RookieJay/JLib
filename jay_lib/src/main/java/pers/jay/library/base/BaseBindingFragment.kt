package pers.jay.library.base

import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class BaseBindingFragment<T : ViewBinding> : BaseFragment() {

    private var _binding: T? = null
    protected val mBinding: T
        get() = _binding!!

    override fun initRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = createBinding(inflater, container)
        return mBinding.root
    }

    private fun createBinding(inflater: LayoutInflater, container: ViewGroup?): T {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        val bindingClass = type as Class<T>
        if (ViewDataBinding::class.java.isAssignableFrom(bindingClass)) {
            val layoutId = getLayoutId()
            if (layoutId == 0) {
                throw InflateException("can not initialize layout")
            }
            _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
            return mBinding
        } else {
            return createViewBindingByReflect(bindingClass, container)
        }
    }

    private fun createViewBindingByReflect(aClass: Class<T>, container: ViewGroup?): T {
        val method = aClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        return method.invoke(null, layoutInflater, container, false) as T
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // 避免内存泄漏，Fragment视图销毁时置空绑定对象
        _binding = null
    }

    // 使用DataBinding的子类需重写此方法以提供布局 ID
    protected open fun getLayoutId(): Int {
        return 0
    }

}