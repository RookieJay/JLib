package pers.jay.library.base.viewbinding

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import pers.jay.library.base.BaseModel
import pers.jay.library.base.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * 基于ViewBinding和ViewModel的基类Activity，利用反射获取特定ViewBinding中的inflate方法填充视图
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseVBVMActivity<VB : ViewBinding, VM : BaseViewModel<out BaseModel>> :
    BaseVBActivity<VB>() {

    /**
     * 当前页面viewModel实例
     */
    protected lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        initViewModelByReflect()
        handleError()
        super.onCreate(savedInstanceState)
    }

    /**
     * 异常处理
     */
    private fun handleError() {
        mViewModel.apply {
            mCoroutineErrorData.observe(this@BaseVBVMActivity, Observer {
                hideLoading()
            })
        }
    }

    /**
     *  反射，获取特定ViewModel泛型的class
     */
    private fun initViewModelByReflect() {
        Log.e(TAG, "initViewModelByReflect")
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val clazz = type.actualTypeArguments[1] as Class<VM>
            mViewModel = ViewModelProvider(this).get(clazz)
        }
    }

}