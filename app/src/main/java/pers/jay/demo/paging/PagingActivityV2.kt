package pers.jay.demo.paging

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pers.jay.demo.R
import pers.jay.demo.data.Article
import pers.jay.demo.databinding.ActivityPagingBinding
import pers.jay.library.base.ext.getView
import pers.jay.library.base.viewbinding.BaseVBVMActivity
import pers.jay.library.ui.rv.BasePagingAdapter
import pers.jay.library.ui.rv.BaseRvAdapter

class PagingActivityV2 : BaseVBVMActivity<ActivityPagingBinding, PagingViewModel>() {

    private val mAdapter =
        BasePagingAdapter<Article, BaseRvAdapter.BaseViewHolder>(R.layout.layout_item_article, onBind = { holder, article, i ->
            holder.getView<TextView>(R.id.tvTitle).text = article.title
            holder.getView<TextView>(R.id.tvDesc).text = article.niceDate
        })

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@PagingActivityV2)
            mAdapter.addLoadStateListener {
                when (it.refresh) {
                    is LoadState.NotLoading -> {
                        LogUtils.d(TAG, "NotLoading")
                        progressBar.visibility = View.INVISIBLE
                        recyclerView.visibility = View.VISIBLE
                    }
                    is LoadState.Loading -> {
                        LogUtils.d(TAG, "Loading")
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.INVISIBLE
                    }
                    is LoadState.Error -> {
                        val state = it.refresh as LoadState.Error
                        progressBar.visibility = View.INVISIBLE
                        showMessage("Load Error: ${state.error.message}")
                        LogUtils.d(TAG, "Error: ${state.error.message}")
                    }
                }
            }
            mBinding.recyclerView.adapter =
                mAdapter.withLoadStateFooter(FootStateAdapter { mAdapter.retry() })
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        lifecycleScope.launch(Dispatchers.IO) {
            LogUtils.d(TAG, "launch current thread: ${Thread.currentThread().name}")
            mViewModel.loadHomeArticles()
                .catch {
                    LogUtils.e(TAG, it.message)
                }
                .collect { pagingData: PagingData<Article> ->
                    LogUtils.d(TAG, "collect current thread: ${Thread.currentThread().name}")
                    mAdapter.submitData(pagingData)
                }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        val keycode = event?.keyCode
        val action = event?.action
        if (keycode == KeyEvent.KEYCODE_MENU && action == KeyEvent.ACTION_UP) {
            scrollToTop()
        }
        return super.dispatchKeyEvent(event)
    }

    private fun scrollToTop() {
        mBinding.apply {
            recyclerView.scrollToPosition(0)
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                mAdapter.notifyItemRemoved(0)
                showMessage("删除第一个")
            }, 3500)
        }
    }

}