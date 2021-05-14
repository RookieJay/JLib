package pers.jay.library.base.viewbinding

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.LogUtils
import pers.jay.library.base.BaseModel
import pers.jay.library.base.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * 基于ViewBinding的Fragment基类
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseVBVMFragment<VB : ViewBinding, VM : BaseViewModel<out BaseModel>> :
    BaseVBFragment<VB>() {

    protected lateinit var mViewModel: VM

    /**
     *  反射，获取特定ViewModel泛型的class
     */
    private fun initViewModelByReflect(): VM {
        Log.e(TAG, "initViewModelByReflect")
        return recursiveFindViewModel(javaClass)
    }

    /**
     * 递归查找当前fragment->父类的泛型参数
     */
    private fun recursiveFindViewModel(fragmentClazz: Class<*>): VM {
        val type = fragmentClazz.genericSuperclass
        return if (type is ParameterizedType) {
            val clazz = type.actualTypeArguments[1] as Class<VM>
            mViewModel = ViewModelProvider(this).get(clazz)
            return mViewModel
        } else {
            val clazz = fragmentClazz.superclass as Class<*>
            recursiveFindViewModel(clazz)
        }
    }

    /**
     * 异常处理
     */
    private fun handleError() {
        mViewModel.apply {
            mCoroutineErrorData.observe(this@BaseVBVMFragment, Observer {
                LogUtils.e("handleError:$it")
                hideLoading()
            })
        }
    }

    /**
     * 当使用常规模式时，重写initViewModel()方法，且useReflect()返回false
     * 否则默认使用反射获取ViewModel
     */
    override fun beforeInit() {
        mViewModel =
            if (useVMReflect()) {
                initViewModelByReflect()
            } else {
                initViewModel()
            }
        handleError()
    }

    open fun useVMReflect(): Boolean {
        return true
    }

    /**
     * 重写此方法使用常规方式获取ViewModel，即使用 ViewModelProvider(this).get(Class<VM>)
     * 默认也是反射实现
     */
    open fun initViewModel(): VM {
//        val method = this.javaClass.getDeclaredMethod("initViewModel")
//        val returnType = method.returnType as Class<VM>
//        mViewModel = ViewModelProvider(this).get(returnType)
        return mViewModel
    }
}