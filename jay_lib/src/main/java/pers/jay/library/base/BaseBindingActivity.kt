package pers.jay.library.base

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import pers.jay.library.utils.BindingUtils

@Suppress("UNCHECKED_CAST")
abstract class BaseBindingActivity<T : ViewBinding> : BaseActivity() {

    private var _binding: T? = null
    protected val mBinding: T
        get() = _binding!!

    override fun getContentView(): View {
        _binding = createBinding()
        return mBinding.root
    }

    @Suppress("UNCHECKED_CAST")
    private fun createBinding(): T {
        val bindingClass = BindingUtils.getBindingClass(javaClass) as Class<T>?
        bindingClass ?: throw RuntimeException("can't find matched binding Class： $javaClass")
        return if (ViewDataBinding::class.java.isAssignableFrom(bindingClass)) {
            try {
                // 检测子类是否重写了布局 ID
                val layoutId = getLayoutId()
                if (layoutId == 0) {
                    throw IllegalStateException("Currently using DataBinding," +
                            "override the getLayoutId() method in the subclass")
                }
                // 使用 DataBindingUtil 进行绑定
                DataBindingUtil.inflate(layoutInflater, layoutId, null, false) as T
            } catch (e: Exception) {
                // 回退到 ViewBinding 的绑定方式,但无法使用DataBinding功能
                createViewBinding(bindingClass)
            }
        } else {
            // 使用 ViewBinding 的反射方式
            createViewBinding(bindingClass)
        }
    }

    private fun createViewBinding(bindingClass: Class<T>): T {
        val method = bindingClass.getMethod("inflate", LayoutInflater::class.java)
        val binding = method.invoke(null, layoutInflater) as T
        setContentView(binding.root)
        return binding
    }

}