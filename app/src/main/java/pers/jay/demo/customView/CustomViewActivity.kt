package pers.jay.demo.customView

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import pers.jay.demo.data.Article
import pers.jay.demo.databinding.ActivityCustomViewBinding
import pers.jay.demo.databinding.ItemRvCustomViewBinding
import pers.jay.library.base.ext.showToast
import pers.jay.library.base.viewbinding.BaseVBActivity
import pers.jay.library.ui.rv.BaseVBAdapter

class CustomViewActivity : BaseVBActivity<ActivityCustomViewBinding>() {

    val mAdapter =
        BaseVBAdapter<String, ItemRvCustomViewBinding>(ItemRvCustomViewBinding::class) { binding, item, _ ->
            LogUtils.d(TAG, "onBind, $item")
            binding.apply {
                button.text = item
                button.setOnClickListener {
                    showToast(item)
                }
            }
        }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            rv.layoutManager = LinearLayoutManager(this@CustomViewActivity)
            rv.adapter = mAdapter

        }

        // for test
        val fragment = CustomViewFragment<Article>().apply {
            arguments = Bundle().apply {
                putParcelable("testKey", Article(title = "testArticle"))
            }
        }
        val fm = supportFragmentManager
        val trans = fm.beginTransaction()
        trans.add(fragment, "tag")
        trans.commit()
    }

    override fun initData(savedInstanceState: Bundle?) {
        mAdapter.setList(listOf("进度条", "刻度尺", "轮播图", "仪表盘", "贝塞尔水波纹"))
    }

}

