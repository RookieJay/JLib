package pers.jay.demo.customView

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.LogUtils
import pers.jay.demo.R
import pers.jay.demo.databinding.ActivityCustomViewBinding
import pers.jay.demo.databinding.ItemRvCustomViewBinding
import pers.jay.library.base.BaseBindingActivity
import pers.jay.library.base.ext.showToast
import pers.jay.library.base.ext.singleClick
import pers.jay.library.ui.rv.BaseVBAdapter

class CustomViewActivity : BaseBindingActivity<ActivityCustomViewBinding>() {

    val mAdapter =
        BaseVBAdapter<CustomView, ItemRvCustomViewBinding>(ItemRvCustomViewBinding::class) { binding, item, _ ->
            LogUtils.d(TAG, "onBind, $item")
            binding.apply {
                button.text = item.cname
                button.singleClick {
                    showToast(item.cname)
                    openChildPage(item)
                }
            }
        }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            rv.layoutManager = LinearLayoutManager(this@CustomViewActivity)
            rv.adapter = mAdapter
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        mAdapter.setList(listOf(
            CustomView.PROGRESS_BAR,
            CustomView.SCALE_RULER,
            CustomView.BANNER,
            CustomView.DASHBOARD,
            CustomView.BEZIER_RIPPLE,
        ))
    }

    fun openChildPage(item: CustomView) {
        LogUtils.d(TAG, "openChildPage")
        val fragment = CustomViewFragment().apply {
            arguments = Bundle().apply {
                putParcelable(CustomViewFragment.VIEW_TYPE, item)
            }
        }
        FragmentUtils.add(supportFragmentManager, fragment, R.id.flContainer, false, true)
    }

}

