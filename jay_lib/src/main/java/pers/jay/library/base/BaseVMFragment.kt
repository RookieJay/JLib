package pers.jay.library.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import pers.jay.library.base.ext.getViewModel
import pers.jay.library.base.repository.BaseRepository
import pers.jay.library.base.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * @Author RookieJay
 * @Time 2021/5/21 18:42
 * @Description 基于[ViewBinding]和[ViewModel]的Fragment基类
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseVMFragment<VB : ViewBinding, VM : BaseViewModel<out BaseRepository>> :
    BaseBindingFragment<VB>() {

    protected lateinit var mViewModel: VM

    /**
     *  反射，获取特定ViewModel泛型的class
     */
    private fun initViewModelByReflect(): VM {
        return recursiveFindViewModel(javaClass)
    }

    /**
     * 递归查找当前fragment->父类的泛型参数
     */
    private fun recursiveFindViewModel(fragmentClazz: Class<*>): VM {
        val type = fragmentClazz.genericSuperclass
        return if (type is ParameterizedType) {
            val clazz = type.actualTypeArguments[1] as Class<VM>
            mViewModel = getViewModel(clazz)
            return mViewModel
        } else {
            val clazz = fragmentClazz.superclass as Class<*>
            recursiveFindViewModel(clazz)
        }
    }

    /**
     * 当使用常规模式时，重写initViewModel()方法，且useReflect()返回false
     * 否则默认使用反射获取ViewModel
     */
    override fun beforeInit(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        mViewModel =
            if (useVMReflect()) {
                initViewModelByReflect()
            } else {
                initViewModel()
            }
    }

    /**
     * 是否使用反射方式初始化ViewModel
     * 默认使用反射实现
     */
    open fun useVMReflect(): Boolean {
        return true
    }

    /**
     * 重写此方法使用常规方式获取ViewModel，即使用 ViewModelProvider(this).get(Class<VM>)
     *
     */
    open fun initViewModel(): VM {
        return mViewModel
    }
}