package pers.jay.demo.paging.base

import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import pers.jay.library.ui.rv.BaseRvAdapter
import pers.jay.library.ui.rv.RecyclerViewUtils.init

fun <T : Any, VH : BaseRvAdapter.BaseViewHolder> RecyclerView.initPagingAdapter(
    layoutManager: RecyclerView.LayoutManager,
    adapter: PagingDataAdapter<T, VH>,
) {
    init(layoutManager, adapter)
}

fun <T : Any, VH : BaseRvAdapter.BaseViewHolder> PagingDataAdapter<T, VH>.addLoadStateListener(
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