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
import pers.jay.demo.databinding.LayoutItemArticleBinding
import pers.jay.library.base.ext.showToast
import pers.jay.library.base.viewbinding.BaseVBVMActivity
import pers.jay.library.ui.rv.BaseVBAdapter
import pers.jay.demo.paging.base.PagingWrapAdapter
import kotlin.random.Random

class PagingActivity : BaseVBVMActivity<ActivityPagingBinding, PagingViewModel>() {

    private val articleAdapter =
        BaseVBAdapter<Article, LayoutItemArticleBinding>(LayoutItemArticleBinding::class) { binding, item, _ ->
            binding.apply {
                item.apply {
                    val text = id.toString() + title
                    tvTitle.text = text
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
        if (action == KeyEvent.ACTION_UP) {
            when (keycode) {
                KeyEvent.KEYCODE_MENU -> {
                    add()
                }
                KeyEvent.KEYCODE_F1 -> {
                    delete()
                }
                KeyEvent.KEYCODE_F3 -> {
                    update()
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    private fun scrollTo(position: Int) {
        mBinding.apply {
            recyclerView.scrollToPosition(0)
        }
    }

    private fun add() {
        val randomPosition = getRandomPos()
        val newData = Article()
        newData.title = "这是我新添加的数据"
        newData.niceDate = "just now"
        articleAdapter.addItem(randomPosition, newData)
        LogUtils.d(TAG, "data size after add:${articleAdapter.getData()[randomPosition]}")
        mAdapter.notifyItemInserted(randomPosition)
        showMessage("add第一个")
        scrollTo(randomPosition)
    }

    private fun getRandomPos(): Int {
        val count = articleAdapter.getData().size
        return Random.nextInt(0, count)
    }

    private fun delete() {
        val randomPosition = getRandomPos()
        LogUtils.d(TAG, "data size before remove:${articleAdapter.getData().size}")
        articleAdapter.removeAt(randomPosition)
        LogUtils.d(TAG, "data size after remove:${articleAdapter.getData().size}")
        mAdapter.notifyItemRemoved(randomPosition)
        showMessage("delete第一个")
        scrollTo(randomPosition)
    }

    private fun update() {
        val randomPosition = getRandomPos()
        val data = articleAdapter.getData()[randomPosition]
        data.title = "我修改了标题"
        articleAdapter.setItem(randomPosition, data)
        LogUtils.d(TAG, "data after update:${articleAdapter.getData()[0]}")
        mAdapter.notifyItemChanged(randomPosition)
        showMessage("update第一个")

        scrollTo(randomPosition)
    }

    private fun query() {
        scrollTo(0)
    }

}