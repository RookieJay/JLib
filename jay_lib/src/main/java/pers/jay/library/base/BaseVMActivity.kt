package pers.jay.library.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import pers.jay.library.base.ext.getViewModel
import pers.jay.library.base.repository.BaseRepository
import pers.jay.library.base.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * @Author RookieJay
 * @Time 2021/5/21 17:53
 * @Description 基于ViewBinding和ViewModel的基类Activity
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseVMActivity<VB : ViewBinding, VM : BaseViewModel<out BaseRepository>> :
    BaseBindingActivity<VB>() {

    /**
     * 当前页面viewModel实例
     */
    protected var _viewModel: VM? = null
    protected val mViewModel: VM get() = _viewModel!!

    override fun beforeInit(savedInstanceState: Bundle?) {
        super.beforeInit(savedInstanceState)
        initViewModelByReflect()
    }

    /**
     *  反射，获取特定ViewModel泛型的class
     */
    private fun initViewModelByReflect() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val clazz = type.actualTypeArguments[1] as Class<VM>
            _viewModel = getViewModel(clazz)
        }
    }

}