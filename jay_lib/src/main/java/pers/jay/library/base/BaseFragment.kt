package pers.jay.library.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
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
        lifecycle.addObserver(FragmentLifecycleLogObserver(TAG))
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
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParams(savedInstanceState)
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
     * 返回键回调
     */
    open fun onBackPressed() {

    }

    override fun getContext() = mContext


}