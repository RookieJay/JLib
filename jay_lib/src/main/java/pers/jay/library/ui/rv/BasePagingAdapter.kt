package pers.jay.library.ui.rv

import androidx.lifecycle.Lifecycle
import androidx.paging.*
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.Dispatchers

/**
 * fixme differ.getItem返回空不知如何处理
 */
class BasePagingAdapter<T : Any, VH : BaseRvAdapter.BaseViewHolder> @JvmOverloads constructor(
    getItemLayoutResId: Int = 0,
    onBind: ((VH, T, Int) -> Unit)? = null,
    differCallback: DiffUtil.ItemCallback<T> = DefaultDifferCallback()
) :
    BaseRvAdapter<T, VH>(getItemLayoutResId, onBind) {

    private val differ = AsyncPagingDataDiffer<T>(
        diffCallback = differCallback,
        updateCallback = AdapterListUpdateCallback(this),
        mainDispatcher = Dispatchers.Main,
        workerDispatcher = Dispatchers.Default
    )

    init {
        differ.addLoadStateListener {
            when (it.append) {
                is LoadState.NotLoading -> {
                    val items = differ.snapshot().items
                    addList(items)
                }

                else -> {
                    LogUtils.d(TAG, "differ.addLoadStateListener, it.append=${it.append}")
                }

            }
        }
    }

    suspend fun submitData(pagingData: PagingData<T>) {
        differ.submitData(pagingData)
    }

    fun submitData(lifecycle: Lifecycle, pagingData: PagingData<T>) {
        differ.submitData(lifecycle, pagingData)
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        super.onBindViewHolder(holder, position)
        // 这一步必不可少，因为Paging就是通过getItem触发预加载的
        differ.getItem(position)!!
    }

    fun addLoadStateListener(listener: (CombinedLoadStates) -> Unit) {
        differ.addLoadStateListener(listener)
    }

    fun withLoadStateFooter(
        footer: LoadStateAdapter<*>
    ): ConcatAdapter {
        addLoadStateListener { loadStates ->
            footer.loadState = loadStates.append
        }
        return ConcatAdapter(this, footer)
    }

    fun retry() {
        differ.retry()
    }

}