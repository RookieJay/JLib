package pers.jay.library.ui.rv

import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils

fun <T : Any, VH : BaseRvAdapter.BaseViewHolder> PagingDataAdapter<T, VH>.test(
    onNotLoading: (() -> Unit)? = null,
    onLoading: (() -> Unit)? = null,
    onError: ((LoadState.Error) -> Unit)? = null
) {
    addLoadStateListener {
        when (it.refresh) {
            is LoadState.NotLoading -> {
                onNotLoading?.invoke()
            }
            is LoadState.Loading -> {
                onLoading?.invoke()
            }
            is LoadState.Error -> {
                val state = it.refresh as LoadState.Error
                LogUtils.d(
                    "PagingDataAdapter[${this.hashCode()}]",
                    "addLoadStateListener Error: ${state.error.message}"
                )
                onError?.invoke(state)
            }
        }
    }
}

object RecyclerViewUtils {

    fun <VH : BaseRvAdapter.BaseViewHolder> RecyclerView.init(
        layoutManager: RecyclerView.LayoutManager,
        adapter: RecyclerView.Adapter<VH>
    ) {
        this.layoutManager = layoutManager
        this.adapter = adapter
        // 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        setHasFixedSize(true)
    }

    fun <T, VH : BaseRvAdapter.BaseViewHolder> RecyclerView.initRV(
        layoutManager: RecyclerView.LayoutManager,
        adapter: BaseRvAdapter<T, VH>
    ) {
        init(layoutManager, adapter)
    }

    fun <T : Any, VH : BaseRvAdapter.BaseViewHolder> RecyclerView.initPagingAdapter(
        layoutManager: RecyclerView.LayoutManager,
        adapter: PagingDataAdapter<T, VH>,
    ) {
        init(layoutManager, adapter)
    }



}