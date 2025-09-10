package pers.jay.demo

import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import pers.jay.demo.binding.TestDB
import pers.jay.demo.customView.CustomViewActivity
import pers.jay.demo.databinding.ActivityMainBinding
import pers.jay.demo.paging.PagingActivity
import pers.jay.demo.viewbinding.DemoActivity
import pers.jay.demo.viewbinding.DemoViewModel
import pers.jay.library.base.BaseVMActivity
import pers.jay.library.base.ext.singleClick
import pers.jay.library.base.ext.startActivity


class MainActivity : BaseVMActivity<ActivityMainBinding, DemoViewModel>() {

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            btRequest.setOnClickListener {
                startActivity<DemoActivity>("id" to 1001, "name" to "test") {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    LogUtils.d(TAG, "启动DemoActivity")
                }
            }
            btInfo.setOnClickListener {
                startActivity<InfoActivity> {

                }
            }
            btCompose.setOnClickListener {
                showMessage("暂未开放")
            }
            btPaging.setOnClickListener {
                startActivity<PagingActivity> {

                }
            }
            btCustView.singleClick {
                startActivity<CustomViewActivity> {

                }
            }
        }
    }


    override fun initData(savedInstanceState: Bundle?) {

    }


}