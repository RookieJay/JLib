package pers.jay.library.base.viewbinding

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import pers.jay.library.base.repository.BaseRepository
import pers.jay.library.base.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * @Author RookieJay
 * @Time 2021/5/21 17:53
 * @Description 基于ViewBinding和ViewModel的基类Activity
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseVBVMActivity<VB : ViewBinding, VM : BaseViewModel<out BaseRepository>> :
    BaseVBActivity<VB>() {

    /**
     * 当前页面viewModel实例
     */
    protected lateinit var mViewModel: VM

    override fun beforeInit(savedInstanceState: Bundle?) {
        super.beforeInit(savedInstanceState)
        initViewModelByReflect()
        handleError()
    }

    /**
     * 异常处理
     */
    private fun handleError() {
        mViewModel.apply {
            mCoroutineErrorData.observe(this@BaseVBVMActivity, Observer {
                showError(it)
            })
        }
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