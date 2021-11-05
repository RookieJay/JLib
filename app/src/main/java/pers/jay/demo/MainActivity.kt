package pers.jay.demo

import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import pers.jay.demo.databinding.ActivityMainBinding
import pers.jay.demo.databinding.InfoActivity
import pers.jay.demo.paging.PagingActivity
import pers.jay.demo.viewbinding.DemoActivity
import pers.jay.demo.viewbinding.DemoViewModel
import pers.jay.library.base.ext.singleClick
import pers.jay.library.base.ext.startActivity
import pers.jay.library.base.viewbinding.BaseVBVMActivity

class MainActivity : BaseVBVMActivity<ActivityMainBinding, DemoViewModel>() {

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            btRequest.setOnClickListener {
                startActivity<DemoActivity> {
                    putExtra("test", "test")
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
                LogUtils.d(TAG, "btCustView singleClick")
//                startActivity<CustomViewActivity> {
//
//                }
            }
        }
    }


    override fun initData(savedInstanceState: Bundle?) {

    }


}