package pers.jay.demo.paging

import android.os.Bundle
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
import pers.jay.library.base.viewbinding.BaseVBVMActivity

class PagingActivity : BaseVBVMActivity<ActivityPagingBinding, PagingViewModel>() {

    private val articleAdapter = ArticleAdapter()

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@PagingActivity)
            articleAdapter.addLoadStateListener {
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
                articleAdapter.withLoadStateFooter(FootStateAdapter { articleAdapter.retry() })
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        lifecycleScope.launch(Dispatchers.IO) {
            LogUtils.d(TAG, "launch current thread: ${Thread.currentThread().name}")
            mViewModel.loadHomeArticles()
                .catch {
                    LogUtils.e(TAG, it.message)
                }.collect { pagingData: PagingData<Article> ->
                    LogUtils.d(TAG, "collect current thread: ${Thread.currentThread().name}")
                    articleAdapter.submitData(pagingData)
                }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        return super.dispatchKeyEvent(event)
    }

}