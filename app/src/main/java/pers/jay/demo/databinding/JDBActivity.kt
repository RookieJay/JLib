package pers.jay.demo.databinding

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import pers.jay.library.base.databinding.BaseDBVMActivity

/**
 * Copyright (c) 2011, 北京视达科技有限责任公司 All rights reserved.
 * author：juncai.zhou
 * date：2021/8/31 09:31
 * description：
 */
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