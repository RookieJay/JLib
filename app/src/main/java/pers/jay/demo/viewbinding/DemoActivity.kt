package pers.jay.demo.viewbinding

import android.os.Bundle
import android.widget.TextView
import com.blankj.utilcode.util.LogUtils
import pers.jay.demo.databinding.ActivityDemoBinding
import pers.jay.demo.net.Daily
import pers.jay.library.base.BaseVMActivity


class DemoActivity : BaseVMActivity<ActivityDemoBinding, DemoViewModel>() {

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.text1.setOnClickListener {
            requestDemo(mBinding.text1)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        requestDemo(mBinding.text1)
        test2(mBinding.text2)
        requestDemo(mBinding.text3)
    }


    private fun requestDemo(textView: TextView) {
        val TAG = textView.id
        LogUtils.i("requestDemo")
        mViewModel.test().observeState(this) {
            onStart {
                LogUtils.d(TAG, "onStart $textView")
                updateText(textView, "onStart")
            }
            onResult { tabs ->
                LogUtils.d(TAG, "onResult:$tabs")
                updateText(textView, tabs.toString())
            }
            onEmpty {
                LogUtils.d(TAG, "onEmpty")
                updateText(textView, "onEmpty")
            }
            onError { exception, msg->
                LogUtils.d(TAG, "onError $exception", "msg:$msg")
                updateText(textView, msg)
            }
            onCompletion {
                LogUtils.d(TAG, "onCompletion ${textView.id}")
            }
        }
    }

    private fun test2(textView: TextView) {
        LogUtils.i("test")
        mViewModel.test2().observeState(this) {
            onStart {
                LogUtils.d(TAG, "onStart $textView")
                updateText(textView, "onStart")
            }
            onResult { tabs: List<Daily> ->
                LogUtils.d(TAG, "onResult:$tabs")
                updateText(textView, tabs.toString())
            }
            onEmpty {
                LogUtils.d(TAG, "onEmpty")
                updateText(textView, "onEmpty")
            }
            onError { exception, msg ->
                LogUtils.d(TAG, "onError $exception")
                updateText(textView, "onError, msg:$msg")
            }
            onCompletion {
                LogUtils.d(TAG, "onCompletion ${textView.id}")
            }
        }
    }

    private fun updateText(textView: TextView, msg: String) {
        textView.apply {
            text = msg
        }
    }

    override fun showError(message: String?) {

    }

}