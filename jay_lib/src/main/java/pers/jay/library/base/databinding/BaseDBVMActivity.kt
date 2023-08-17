package pers.jay.library.base.databinding

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

/**
 * 基于DataBinding和ViewModel的基类Activity
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseDBVMActivity<DB : ViewDataBinding, VM : ViewModel>: BaseDBActivity<DB>() {

    protected lateinit var mViewModel : VM

    override fun beforeInit(savedInstanceState: Bundle?) {
        initViewModelByReflect()
    }

    /**
     *  反射，获取特定ViewModel泛型的class
     */
    private fun initViewModelByReflect() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val clazz = type.actualTypeArguments[1] as Class<VM>
            mViewModel = ViewModelProvider(this).get(clazz)
        }
    }

}