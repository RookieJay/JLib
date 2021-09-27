package pers.jay.demo.databinding

import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import pers.jay.demo.R
import pers.jay.demo.common.Const
import pers.jay.library.base.databinding.BaseDBVMActivity
import pers.jay.library.loadsir.StatusCallback

/**
 * databinding示例
 */
class InfoActivity : BaseDBVMActivity<ActivityInfoBinding, InfoViewModel>() {

    override var enableActivityLoadSir = true

    override var mActivityStatusCallback: StatusCallback?
        get() = Const.DEFAULT_ACTIVITY_STATUS_CALLBACK
        set(value) {}

    override fun initLayout(savedInstanceState: Bundle?): Int {
        return R.layout.activity_info
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData(savedInstanceState: Bundle?) {
        loadData()
    }

    private fun loadData() {
        // 对activity进行视图回调处理,enableActivityLoadSir要覆写
        mViewModel.loadData().observeOnActivity {
            onStart {
                LogUtils.e(TAG, "onStart")
            }
            onSuccess { data ->
                mBinding.tab = data[0]
            }
            onError {
                LogUtils.e(TAG, "error $it")
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