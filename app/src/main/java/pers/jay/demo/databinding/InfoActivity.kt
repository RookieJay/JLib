package pers.jay.demo.databinding

import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import pers.jay.demo.R
import pers.jay.demo.loadsir.ErrorCallback
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

            }
            onSuccess { data ->
                mLoadService?.showSuccess()
                mBinding.tab = data[0]
            }
            onError {
                LogUtils.e(TAG, "error $it")
                showError(it)
            }
            onCompletion {
                hideLoading()
            }
        }
    }

    override fun showLoading() {
        super.showLoading()

    }

    override fun showError(message: String?) {
        super.showError(message)
        mLoadService?.showCallback(ErrorCallback::class.java)
    }

    override fun hideLoading() {
        super.hideLoading()
//        mLoadService?.showCallback(SuccessCallback::class.java)
    }

}