package pers.jay.demo.viewbinding

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pers.jay.demo.data.Tab
import pers.jay.demo.databinding.ActivityDemoBinding
import pers.jay.library.base.viewbinding.BaseVBVMActivity

class DemoActivity : BaseVBVMActivity<ActivityDemoBinding, DemoViewModel>() {


    override fun initView(savedInstanceState: Bundle?) {
        mBinding.text1.setOnClickListener {
            requestDemo(mBinding.text1)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        requestDemo(mBinding.text1)
        requestDemo(mBinding.text2)
        lifecycleScope.launch(Dispatchers.Main) {
            delay(1000)
            requestDemo(mBinding.text3)
        }

    }


    private fun requestDemo(textView: TextView) {
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
            onError { msg ->
                LogUtils.d(TAG, "onError, msg:$msg")
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