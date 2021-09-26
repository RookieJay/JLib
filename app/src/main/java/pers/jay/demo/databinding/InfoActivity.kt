package pers.jay.demo.databinding

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.LogUtils
import pers.jay.demo.R
import pers.jay.demo.common.Const
import pers.jay.library.base.databinding.BaseDBVMActivity

/**
 * databinding示例
 */
class InfoActivity : BaseDBVMActivity<ActivityInfoBinding, InfoViewModel>() {

    override var enableLoadSir = false

    override fun initLayout(savedInstanceState: Bundle?): Int {
        return R.layout.activity_info
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData(savedInstanceState: Bundle?) {
        loadData(mBinding.tvTabName)
    }

    private fun loadData(view: View) {
        mViewModel.loadData().observeState(view, Const.DEFAULT_LOADSIR_CALLBACK, this) {
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
            onReload {
                loadData(view)
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