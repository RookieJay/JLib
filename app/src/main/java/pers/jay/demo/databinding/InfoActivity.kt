package pers.jay.demo.databinding

import android.os.Bundle
import pers.jay.demo.R
import pers.jay.demo.Tab
import pers.jay.demo.TestStateObserver
import pers.jay.library.base.databinding.BaseDBVMActivity

/**
 * databinding示例
 */
class InfoActivity : BaseDBVMActivity<ActivityInfoBinding, InfoViewModel>() {

    override fun initLayout(savedInstanceState: Bundle?): Int {
        return R.layout.activity_info
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData(savedInstanceState: Bundle?) {
        mViewModel.loadData().observe(this, object : TestStateObserver<Tab>(mBinding.tvTabName) {
            override fun onSuccess(data: Tab) {
                super.onSuccess(data)
                mBinding.tab = data
            }
        })
    }

}