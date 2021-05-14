package pers.jay.library.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pers.jay.library.lifecycle.ActivityLifecycleObserver

abstract class BaseActivity : AppCompatActivity(), IView {

    protected lateinit var mContext : Context

    protected val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 生命周期日志记录
        lifecycle.addObserver(ActivityLifecycleObserver(TAG))
        mContext = this
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val fragments = supportFragmentManager.fragments
        for (fragment in fragments) {
            if (fragment is BaseFragment) {
                fragment.onBackPressed()
            }
        }
    }


}