package pers.jay.demo.databinding

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import pers.jay.library.base.databinding.BaseDBVMActivity

abstract class JDBActivity<DB : ViewDataBinding, VM : ViewModel>: BaseDBVMActivity<DB, VM> () {

    override fun showEmpty() {
        super.showEmpty()
    }

    override fun showError(message: String?) {
        super.showError(message)
    }

    override fun showLoading() {
        super.showLoading()
    }

    override fun showNoNetwork() {
        super.showNoNetwork()
    }

}