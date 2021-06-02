package pers.jay.demo

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.LogUtils
import pers.jay.demo.databinding.ActivityMainBinding
import pers.jay.library.base.viewbinding.BaseVBVMActivity

class MainActivity : BaseVBVMActivity<ActivityMainBinding, DemoViewModel>() {

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.btRequest.setOnClickListener {
            mViewModel.test().observe(this, object : TestStateObserver<List<Tab>>(mBinding.text) {
                override fun onSuccess(data: List<Tab>?) {
                    super.onSuccess(data)
                    LogUtils.d(TAG, data.toString())
                    mBinding.text.text =
                        String.format("数据请求成功，tab数量【%s】,第一个是【%s】", data?.size, data?.get(0)?.name)
                }

                override fun onDataEmpty() {
                    super.onDataEmpty()
                    LogUtils.d(TAG, "onDataEmpty")
                }

                override fun onError(msg: String?) {
                    super.onError(msg)
                    LogUtils.d(TAG, "onError, msg:$msg")
                }

                override fun onReload(v: View?) {
                    super.onReload(v)
                    LogUtils.d(TAG, "onReload")
                }
            })
        }
    }

    override fun initData(savedInstanceState: Bundle?) {

    }


}