package pers.jay.demo.databinding

import android.os.Bundle
import pers.jay.demo.R
import pers.jay.demo.loadsir.LoadingCallback
import pers.jay.library.base.databinding.BaseDBVMActivity

/**
 * databinding示例
 */
class InfoActivity : BaseDBVMActivity<ActivityInfoBinding, InfoViewModel>() {

    override var enableLoadSir = true

    override fun initLayout(savedInstanceState: Bundle?): Int {
        return R.layout.activity_info
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData(savedInstanceState: Bundle?) {
        mViewModel.loadData().observeState(this) {
            onStart {
                mLoadService?.showCallback(LoadingCallback::class.java)
            }
            onSuccess { data ->
                mLoadService?.showSuccess()
                mBinding.tab = data
            }
        }
    }

}