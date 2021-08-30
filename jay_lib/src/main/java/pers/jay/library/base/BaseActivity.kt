package pers.jay.library.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import pers.jay.library.app.BaseApplication
import pers.jay.library.lifecycle.LifecycleLogObserver

/**
 * @Author RookieJay
 * @Time 2021/5/21 18:20
 * @Description Activity基类
 */
abstract class BaseActivity : AppCompatActivity(), IActivity {

    protected val TAG: String = javaClass.simpleName

    private lateinit var mContext : Context

    override fun getContext() = mContext

    protected var mLoadService: LoadService<Any>? = null

    /**
     * [LoadSir]开关，默认关闭
     */
    open var enableLoadSir = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 生命周期日志记录
        if (BaseApplication.instance().isDebug()) {
            lifecycle.addObserver(LifecycleLogObserver(TAG))
        }
        mContext = this
        initParams(intent)
    }

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

    /**
     * 初始化LoadSir，需要放在[setContentView]之后
     */
    protected fun initLoadSir() {
        if (!enableLoadSir) {
            return
        }
        mLoadService = LoadSir.getDefault().register(this) {
            view -> onReload(view)
        }
    }

    override fun onReload(view: View) {
        LogUtils.i(TAG, "onReload, view:", view.hashCode())
    }


}