package pers.jay.library.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import pers.jay.library.app.BaseApplication
import pers.jay.library.lifecycle.FragmentLifecycleLogObserver

/**
 * @Author RookieJay
 * @Time 2021/5/21 18:19
 * @Description Fragment基类，
 */
abstract class BaseFragment : Fragment(), IFragment {

    private lateinit var mContext: Context

    private var isViewCreated // 界面是否已创建完成
            = false
    private var isDataLoaded // 数据是否已请求
            = false

    companion object {

        @JvmField
        val TAG: String = BaseFragment::class.java.simpleName

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (BaseApplication.instance().isDebug()) {
            lifecycle.addObserver(FragmentLifecycleLogObserver(TAG))
        }
        // 这里监听生命周期,执行懒加载。
        // 在androidx中，onResume回调只会发生在fragment可见时
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                if (enableLazyLoad() && !isDataLoaded) {
                    lazyLoadData()
                    isDataLoaded = true
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParams(arguments)
    }

    /**
     * 是否启用懒加载，默认不开启
     * @return true:开启 false:关闭
     */
    open fun enableLazyLoad(): Boolean {
        return false
    }


    /**
     * 懒加载数据
     */
    open fun lazyLoadData() {

    }

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

    override fun getContext() = mContext


}