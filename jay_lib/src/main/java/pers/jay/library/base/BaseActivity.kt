package pers.jay.library.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pers.jay.library.lifecycle.ActivityLifecycleLogObserver

/**
 * @Author RookieJay
 * @Time 2021/5/21 18:20
 * @Description Activity基类
 */
abstract class BaseActivity : AppCompatActivity(), IActivity {

    private lateinit var mContext : Context

    override fun getContext() = mContext

    protected val TAG: String = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 生命周期日志记录
        lifecycle.addObserver(ActivityLifecycleLogObserver(TAG))
        mContext = this
        initParams(savedInstanceState)
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

}