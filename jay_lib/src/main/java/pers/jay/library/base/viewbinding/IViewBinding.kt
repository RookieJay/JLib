package pers.jay.library.base.viewbinding

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * @Author RookieJay
 * @Time 2021/5/21 17:00
 * @Description 使用ViewBinding页面的顶层接口
 */
interface IViewBinding<VB : ViewBinding> {

    /**
     * 是否使用反射初始化布局
     */
    fun useVBReflect(): Boolean

    /**
     * 通过反射初始化布局
     * 当[IViewBinding [useVBReflect]]返回true时必须重写此方法
     * @param container fragment需要的
     * @return ViewBinding实例
     */
    fun initRootViewByReflect(container: ViewGroup? = null): VB? {
        return null
    }

    /**
     * 常规方式初始化布局
     * 当[IViewBinding [useVBReflect]]返回false时必须重写此方法
     * @param container fragment需要的
     * @return ViewBinding实例
     */
    fun initRootViewCommon(container: ViewGroup? = null): VB? {
        return null
    }


}