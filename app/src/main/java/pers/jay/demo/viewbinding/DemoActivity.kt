package pers.jay.demo.viewbinding

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pers.jay.demo.common.Const
import pers.jay.demo.databinding.ActivityDemoBinding
import pers.jay.demo.loadsir.RetryCallback
import pers.jay.demo.vm.DemoViewModel
import pers.jay.library.base.viewbinding.BaseVBVMActivity
import pers.jay.library.loadsir.StatusCallback

class DemoActivity : BaseVBVMActivity<ActivityDemoBinding, DemoViewModel>() {

    override var enableActivityLoadSir = false

    override var mActivityStatusCallback: StatusCallback? = Const.DEFAULT_ACTIVITY_STATUS_CALLBACK

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.text1.setOnClickListener {
            requestDemo(mBinding.text1)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        requestDemo(mBinding.text1)
//        requestDemo(mBinding.text2)
        lifecycleScope.launch(Dispatchers.Main) {
            delay(1000)
            requestDemo(mBinding.text3)
        }

    }


    private fun requestDemo(textView: TextView) {

        // 单独对view视图处理，activity不应开启loadsir注册
        mViewModel.test().observeState(textView, Const.DEFAULT_VIEW_STATUS_CALLBACK, this) {
            onStart {
                LogUtils.d(TAG, "onStart")
            }
            onSuccess { tabs ->
                LogUtils.d(TAG, tabs.toString())
                textView.apply {
                    text = tabs.toString()
                }
            }
            onEmpty {
                LogUtils.d(TAG, "onEmpty")
            }
            onError { msg ->
                LogUtils.d(TAG, "onError, msg:$msg")
            }
            onCompletion {
                LogUtils.d(TAG, "onCompletion ${textView.id}")
            }
        }
    }

    override fun showError(message: String?) {
        mLoadService?.showCallback(RetryCallback::class.java)
    }

}