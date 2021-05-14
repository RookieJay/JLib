package pers.jay.library.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * 基于ViewBinding的基类Activity，利用反射获取特定ViewBinding中的inflate方法填充视图
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseVBActivity<VB : ViewBinding> : BaseActivity() {

    // 这里使用了委托，表示只有使用到instance才会执行该段代码
    protected val instance by lazy { this }

    /**
     * 页面布局绑定，所有视图从这里获取
     */
    protected lateinit var mBinding: VB


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        initParams()
        init(savedInstanceState)
    }

    open fun initParams() {

    }

    private fun init(savedInstanceState: Bundle?) {
        initRootView()
        initView(savedInstanceState)
        initData(savedInstanceState)

    }

    /**
     *  反射，调用特定ViewBinding中的inflate方法填充视图
     */
    private fun initRootView() {
        Log.i(TAG, "initRootView")
        val type = javaClass.genericSuperclass
        // ParameterizedType 参数化类型 声明类型中带有“<>”的都是参数化类型
        if (type is ParameterizedType) {
            val clazz = type.actualTypeArguments[0] as Class<VB>
            val method = clazz.getMethod("inflate", LayoutInflater::class.java)
            mBinding = method.invoke(null, layoutInflater) as VB
            setContentView(mBinding.root)
            Log.i(TAG, "initRootView finish.")
        }
    }
}