package pers.jay.library.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import pers.jay.library.app.BaseApplication
import pers.jay.library.base.livedata.StateLiveData
import pers.jay.library.lifecycle.LifecycleLogObserver
import pers.jay.library.loadsir.StatusCallback

/**
 * @Author RookieJay
 * @Time 2021/5/21 18:20
 * @Description Activity基类
 */
abstract class BaseActivity : AppCompatActivity(), IActivity {

    protected val TAG: String = javaClass.simpleName

    private lateinit var mContext: Context

    override fun getContext() = mContext


    /**
     * [LoadSir]开关，默认关闭
     */
    open var enableActivityLoadSir = false

    protected var mLoadService: LoadService<Any>? = null

    open var mActivityStatusCallback: StatusCallback? = null

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
        if (!enableActivityLoadSir) {
            return
        }
        if (mActivityStatusCallback == null) {
            throw IllegalArgumentException("You must specify Activity StatusCallback when enable LoadSir")
        }
        mLoadService = LoadSir.getDefault().register(this) { view ->
            onReload(view)
        }
    }

    override fun onReload(view: View) {
        LogUtils.i(TAG, "onReload, view:", view.hashCode())
    }

    override fun showLoading() {
        super.showLoading()
        mLoadService?.showCallback(mActivityStatusCallback?.loadingCallback)
    }

    override fun hideLoading() {
        super.hideLoading()
        mLoadService?.setCallBack(mActivityStatusCallback?.loadingCallback) { context, view ->
            view.visibility = View.GONE
        }
    }

    override fun showEmpty() {
        super.showEmpty()
        mLoadService?.showCallback(mActivityStatusCallback?.emptyCallback)
    }

    override fun showError(message: String?) {
        super.showError(message)
        mLoadService?.showCallback(mActivityStatusCallback?.errorCallback)
    }

    override fun showSuccess() {
        super.showSuccess()
        mLoadService?.showSuccess()
    }

    /**
     * 设计目的是为了解决在activity中
     */
    fun <T> StateLiveData<T>.observeOnActivity(
        showLoading: Boolean = true,
        listenerBuilder: StateListener<T>.() -> Unit
    ) {
        autoObserve(showLoading, this, mActivityStatusCallback!!, listenerBuilder)
    }
    /**
     * @desc 发起请求并根据结果自动观察，完成相应视图切换
     * @param  statusView 为空时，执行activity的loadSir逻辑，否则交由[StateLiveData]对view进行处理
     * @return
     */
    fun <T> autoObserve(
        showLoading: Boolean = true,
        liveData: StateLiveData<T>,
        statusCallback: StatusCallback,
        listenerBuilder: StateListener<T>.() -> Unit
    ) {
        val stateListener = StateListener<T>().also(listenerBuilder)
        liveData.observeState(null, statusCallback, this@BaseActivity) {
            onStart {
                if (showLoading) {
                    showLoading()
                }
                stateListener.startAction?.invoke()
            }
            onSuccess { data ->
                showSuccess()
                stateListener.successAction?.invoke(data)
            }
            onEmpty {
                LogUtils.d(TAG, "onEmpty")
                showEmpty()
                stateListener.emptyAction?.invoke()
            }
            onError { msg ->
                LogUtils.d(TAG, "onError, msg:$msg")
                showError(msg)
                stateListener.errorAction?.invoke(msg)
            }
            onCompletion {
                LogUtils.d(TAG, "onCompletion")
                hideLoading()
                stateListener.completeAction?.invoke()
            }
            onReload {
                LogUtils.d(TAG, "onReload")
                autoObserve(showLoading, liveData, statusCallback, listenerBuilder)
            }
        }
    }

}