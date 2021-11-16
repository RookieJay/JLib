package pers.jay.demo.paging

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pers.jay.demo.data.Article
import pers.jay.demo.databinding.ActivityPagingBinding
import pers.jay.demo.databinding.LayoutItemArticleBinding
import pers.jay.library.base.ext.showToast
import pers.jay.library.base.viewbinding.BaseVBVMActivity
import pers.jay.library.ui.rv.BaseVBAdapter
import pers.jay.library.ui.rv.PagingWrapAdapter

class PagingActivity : BaseVBVMActivity<ActivityPagingBinding, PagingViewModel>() {

    private val articleAdapter =
        BaseVBAdapter<Article, LayoutItemArticleBinding>(LayoutItemArticleBinding::class) { binding, item, _ ->
            binding.apply {
                item.apply {
                    tvTitle.text = title
                    tvDesc.text = niceDate
                }
            }
            binding.root.setOnClickListener {
                item.title?.let { it1 -> showToast(it1) }
            }
        }

    val mAdapter =
        PagingWrapAdapter<Article, BaseVBAdapter.BaseVBViewHolder<LayoutItemArticleBinding>>(
            articleAdapter
        ) {
            articleAdapter.addList(it)
        }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {

            recyclerView.layoutManager = LinearLayoutManager(this@PagingActivity)
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
                articleAdapter.removeAt(0)
                mAdapter.notifyItemRemoved(0)
                showMessage("删除第一个")
            }, 3500)
        }
    }

}