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
import pers.jay.demo.vm.DemoViewModel
import pers.jay.library.base.viewbinding.BaseVBVMActivity

class DemoActivity : BaseVBVMActivity<ActivityDemoBinding, DemoViewModel>() {

    override var enableLoadSir = false

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.text1.setOnClickListener {
            requestDemo(mBinding.text1)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        requestDemo(mBinding.text1)
//        requestDemo(mBinding.text2)
        lifecycleScope.launch(Dispatchers.Main) {
            delay(2000)
            requestDemo(mBinding.text3)
        }

    }


    private fun requestDemo(textView: TextView) {
        mViewModel.test().observeState(textView, Const.DEFAULT_LOADSIR_CALLBACK, this) {
            onStart {
//                mLoadService?.showCallback(LoadingCallback::class.java)
            }
            onSuccess { tabs ->
                LogUtils.d(TAG, tabs.toString())
                textView.apply{
                    text = tabs.toString()
                }
//                mLoadService?.showSuccess()
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
            onReload {
                LogUtils.d(TAG, "onReload")
                requestDemo(textView)
            }
        }
    }

}