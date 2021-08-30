package pers.jay.demo.viewbinding

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pers.jay.demo.databinding.ActivityDemoBinding
import pers.jay.demo.loadsir.EmptyCallback
import pers.jay.demo.loadsir.ErrorCallback
import pers.jay.demo.loadsir.LoadingCallback
import pers.jay.demo.vm.DemoViewModel
import pers.jay.library.base.viewbinding.BaseVBVMActivity
import pers.jay.library.loadsir.ViewStatusCallback

class DemoActivity : BaseVBVMActivity<ActivityDemoBinding, DemoViewModel>() {

    override var enableLoadSir = false

    val callback = ViewStatusCallback(LoadingCallback::class.java, EmptyCallback::class.java, ErrorCallback::class.java)

    override fun initView(savedInstanceState: Bundle?) {

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
        mViewModel.test().observeState(textView, callback, this) {
            onStart {
                mLoadService?.showCallback(LoadingCallback::class.java)
            }
            onSuccess { tabs ->
                LogUtils.d(TAG, tabs.toString())
                textView.apply{
                    text = tabs.toString()
                }
                mLoadService?.showSuccess()
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
            }
        }
    }

}