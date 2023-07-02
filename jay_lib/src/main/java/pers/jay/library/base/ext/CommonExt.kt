package pers.jay.library.base.ext

import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import pers.jay.library.ui.rv.BaseRvAdapter

fun showToast(message: String) = ToastUtils.showShort(message)

fun showToast(format: String, args: Any?) = ToastUtils.showShort(format, args)

fun showLongToast(message: String) = ToastUtils.showLong(message)

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