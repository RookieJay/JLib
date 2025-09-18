package pers.jay.demo.databinding

import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import pers.jay.demo.R
import pers.jay.demo.data.Tab
import pers.jay.library.base.BaseVMActivity

/**
 * databinding示例
 */

class InfoActivity : BaseVMActivity<ActivityInfoBinding, InfoViewModel>() {


    override fun getLayoutId(): Int {
        return R.layout.activity_info
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData(savedInstanceState: Bundle?) {
        loadData()
    }

    private fun loadData() {
        // 对activity进行视图回调处理,enableActivityLoadSir要覆写
        mViewModel.loadData().observeState(this) {
            onStart {
                LogUtils.e(TAG, "onStart")
            }
            onResult { it ->
                mBinding.tab = it[0]
            }
            onError { _, it ->
                LogUtils.e(TAG, "onError: $it")
                val errorTab = Tab()
                errorTab.name = it
                mBinding.tab = errorTab
            }
        }
    }

    override fun showLoading() {
        super.showLoading()

    }

    override fun showError(message: String?) {
        super.showError(message)
//        mLoadService?.showCallback(RetryCallback::class.java)
    }

    override fun hideLoading() {
        super.hideLoading()
//        mLoadService?.showCallback(SuccessCallback::class.java)
    }

}