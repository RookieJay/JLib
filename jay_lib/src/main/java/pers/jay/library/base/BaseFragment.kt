package pers.jay.library.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import pers.jay.library.app.BaseApplication
import pers.jay.library.lifecycle.LifecycleLogObserver

/**
 * @Author RookieJay
 * @Time 2021/5/21 18:19
 * @Description Fragment基类，
 */
abstract class BaseFragment : Fragment(), IFragment {

    protected val TAG: String = javaClass.simpleName

    private lateinit var mContext: Context

    private var isViewCreated // 界面是否已创建完成
            = false
    private var isDataLoaded // 数据是否已请求
            = false

    /**
     * 是否启用懒加载，默认不开启
     * @return true:开启 false:关闭
     */
    open val enableLazyLoad: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (BaseApplication.instance.isDebug()) {
            lifecycle.addObserver(LifecycleLogObserver(TAG))
        }
        // 这里监听生命周期,执行懒加载。
        // 在androidx中，onResume回调只会发生在fragment可见时
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                if (enableLazyLoad && !isDataLoaded) {
                    lazyLoadData()
                    isDataLoaded = true
                }
            }
        })
        initArgs(arguments)
    }

    override fun getContext() = mContext

    /**
     * 初始化参数
     */
    open fun initArgs(arguments: Bundle?) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParams(arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        beforeInit(inflater, container, savedInstanceState)
        return initRootView(inflater, container)
    }

    /**
     * 初始化之前的逻辑
     */
    open fun beforeInit(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
    }

    /**
     * 初始化根布局
     */
    abstract fun initRootView(inflater: LayoutInflater, container: ViewGroup?): View

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        isViewCreated = true
        // fragment点击穿透解决办法:从事件分发的角度来解决。
        root.isClickable = true
        initView(savedInstanceState)
        initData(savedInstanceState)
    }


    /**
     * 懒加载数据
     */
    open fun lazyLoadData() {}

    /**
     * 返回键回调，分发给本Fragment下的各个子Fragment
     */
    open fun onBackPressed() {
        val subFragments = childFragmentManager.fragments
        for (subFragment in subFragments) {
            if (subFragment is BaseFragment && subFragment.isVisible) {
                subFragment.onBackPressed()
            }
        }
    }

}