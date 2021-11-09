package pers.jay.demo.customView

import android.os.Bundle
import android.os.Parcelable
import com.blankj.utilcode.util.LogUtils
import pers.jay.demo.databinding.ActivityCustomViewBinding
import pers.jay.library.base.viewbinding.BaseVBFragment

class CustomViewFragment<T: Parcelable>(): BaseVBFragment<ActivityCustomViewBinding>() {

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initParams(bundle: Bundle?) {
        super.initParams(bundle)
        val data = bundle?.getParcelable<T>("testKey")
        LogUtils.d(TAG, "data ${data.toString()}")
    }

}