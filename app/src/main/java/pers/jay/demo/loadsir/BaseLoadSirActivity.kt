package pers.jay.demo.loadsir

import android.content.Context
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import pers.jay.library.app.BaseApplication
import pers.jay.library.base.BaseActivity
import pers.jay.library.base.StateListener
import pers.jay.library.base.livedata.StateLiveData
import pers.jay.library.lifecycle.LifecycleLogObserver

/**
 * @Author RookieJay
 * @Time 2021/5/21 18:20
 * @Description Activity基类
 */
abstract class BaseLoadSirActivity : BaseActivity() {

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
        if (BaseApplication.instance.isDebug()) {
            lifecycle.addObserver(LifecycleLogObserver(TAG))
        }
        mContext = this
        initParams(intent)
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

    fun <T> LoadSirLiveData<T>.observeOnActivity(
        showLoading: Boolean = true,
        listenerBuilder: StateListener<T>.() -> Unit
    ) {
        autoObserve(showLoading, this, mActivityStatusCallback!!, listenerBuilder)
    }
    /**
     * 发起请求并根据结果自动观察，完成相应视图切换
     * @param  statusView 为空时，执行activity的loadSir逻辑，否则交由[StateLiveData]对view进行处理
     * @return
     */
    fun <T> autoObserve(
        showLoading: Boolean = true,
        liveData: LoadSirLiveData<T>,
        statusCallback: StatusCallback,
        listenerBuilder: StateListener<T>.() -> Unit
    ) {
        val stateListener = StateListener<T>().also(listenerBuilder)
        liveData.observeState(null, statusCallback, this@BaseLoadSirActivity) {
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
        }
    }

}