package pers.jay.library.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import pers.jay.library.app.BaseApplication
import pers.jay.library.lifecycle.LifecycleLogObserver

/**
 * @Author RookieJay
 * @Time 2021/5/21 18:20
 * @Description Activity基类
 */
abstract class BaseActivity : AppCompatActivity(), IActivity {

    protected val TAG: String = javaClass.simpleName

    private lateinit var mContext: Context

    override fun getContext() = mContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 生命周期日志记录
        if (BaseApplication.instance.isDebug()) {
            lifecycle.addObserver(LifecycleLogObserver(TAG))
        }
        mContext = this
        beforeInit(savedInstanceState)
        setContentView(getContentView())
        initView(savedInstanceState)
        initData(savedInstanceState)
    }

    open fun beforeInit(savedInstanceState: Bundle?) {}

    abstract fun getContentView(): View


    /**
     * 对当前Activity关联的可见fragment做返回键响应
     */
    override fun onBackPressed() {
        super.onBackPressed()
        val fragments = supportFragmentManager.fragments
        for (fragment in fragments) {
            if (fragment is BaseFragment && fragment.isVisible) {
                fragment.onBackPressed()
            }
        }
    }


    override fun onReload(view: View) {
        LogUtils.i(TAG, "onReload, view:", view.hashCode())
    }

}