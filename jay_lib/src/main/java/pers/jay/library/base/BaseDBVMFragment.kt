package pers.jay.library.base

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

/**
 * 基于DataBinding和ViewModel的Fragment基类
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseDBVMFragment<DB : ViewDataBinding, VM : ViewModel> : BaseDBFragment<DB>() {

    protected lateinit var mViewModel: VM

    override fun beforeInit() {
        initViewModelByReflect()
    }

    /**
     *  反射，获取特定ViewModel泛型的class
     */
    private fun initViewModelByReflect() {
        Log.d(TAG, "initViewModelByReflect")
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val clazz = type.actualTypeArguments[1] as Class<VM>
            mViewModel = ViewModelProvider(this).get(clazz)
        }
    }

}