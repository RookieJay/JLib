package pers.jay.demo.paging.base

import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.lifecycle.Lifecycle
import androidx.paging.*
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import pers.jay.library.ui.rv.DefaultDifferCallback

/**
 * 装饰模式，实现在不修改原有基类的基础上，支持Paging
 * fixme 存在以下问题：
 *      1.无法正常使用innerAdapter的数据操作来更新列表数据；
 *      2.向上回滚速度过快时，出现indexOutOfBoundsException问题Index: 0, Size: 0
*/
open class PagingWrapAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    private val innerAdapter: RecyclerView.Adapter<VH>,
    differCallback: DiffUtil.ItemCallback<T> = DefaultDifferCallback(),
    private val callBack: (List<T>) -> Unit
) : RecyclerView.Adapter<VH>() {

    private val differ = AsyncPagingDataDiffer<T>(
        diffCallback = differCallback,
        updateCallback = AdapterListUpdateCallback(this),
        mainDispatcher = Dispatchers.Main,
        workerDispatcher = Dispatchers.Default
    )

    /**
     * Track whether developer called [setStateRestorationPolicy] or not to decide whether the
     * automated state restoration should apply or not.
     */
    private var userSetRestorationPolicy = false

    override fun setStateRestorationPolicy(strategy: StateRestorationPolicy) {
        userSetRestorationPolicy = true
        super.setStateRestorationPolicy(strategy)
    }

    init {
        // Wait on state restoration until the first insert event.
        super.setStateRestorationPolicy(StateRestorationPolicy.PREVENT)

        fun considerAllowingStateRestoration() {
            if (stateRestorationPolicy == StateRestorationPolicy.PREVENT && !userSetRestorationPolicy) {
                this@PagingWrapAdapter.stateRestorationPolicy = StateRestorationPolicy.ALLOW
            }
        }

        // Watch for adapter insert before triggering state restoration. This is almost redundant
        // with loadState below, but can handle cached case.
        @Suppress("LeakingThis")
        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                considerAllowingStateRestoration()
                unregisterAdapterDataObserver(this)
                super.onItemRangeInserted(positionStart, itemCount)
            }
        })

        // Watch for loadState update before triggering state restoration. This is almost
        // redundant with data observer above, but can handle empty page case.
        addLoadStateListener(object : Function1<CombinedLoadStates, Unit> {
            // Ignore the first event we get, which is always the initial state, since we only
            // want to observe for Insert events.
            private var ignoreNextEvent = true

            override fun invoke(loadStates: CombinedLoadStates) {
                if (ignoreNextEvent) {
                    ignoreNextEvent = false
                } else if (loadStates.source.refresh is LoadState.NotLoading) {
                    considerAllowingStateRestoration()
                    removeLoadStateListener(this)
                }
            }
        })


        differ.addLoadStateListener {
            if (it.append is LoadState.NotLoading) {
                val items = differ.snapshot().items
                callBack.invoke(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return innerAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        // Paging通过getItem触发预加载
        // FIXME: 2022/1/21 此处有数组越界,复现步骤：更新界面数据后，不断上下来回滚动
        differ.getItem(position)
        return innerAdapter.onBindViewHolder(holder, position)
    }

    override fun getItemCount(): Int {
        return innerAdapter.itemCount
    }

    suspend fun submitData(pagingData: PagingData<T>) {
        differ.submitData(pagingData)
    }

    fun submitData(lifecycle: Lifecycle, pagingData: PagingData<T>) {
        differ.submitData(lifecycle, pagingData)
    }

    fun retry() {
        differ.retry()
    }

    fun refresh() {
        differ.refresh()
    }

    fun peek(@IntRange(from = 0) index: Int) = differ.peek(index)

    /**
     * Returns a new [ItemSnapshotList] representing the currently presented items, including any
     * placeholders if they are enabled.
     */
    fun snapshot(): ItemSnapshotList<T> = differ.snapshot()

    val loadStateFlow: Flow<CombinedLoadStates> = differ.loadStateFlow

    fun addLoadStateListener(listener: (CombinedLoadStates) -> Unit) {
        differ.addLoadStateListener(listener)
    }

    /**
     * Remove a previously registered [CombinedLoadStates] listener.
     *
     * @param listener Previously registered listener.
     * @see addLoadStateListener
     */
    fun removeLoadStateListener(listener: (CombinedLoadStates) -> Unit) {
        differ.removeLoadStateListener(listener)
    }

    /**
     * Create a [ConcatAdapter] with the provided [LoadStateAdapter]s displaying the
     * [LoadType.PREPEND] [LoadState] as a list item at the end of the presented list.
     *
     * @see LoadStateAdapter
     * @see withLoadStateHeaderAndFooter
     * @see withLoadStateFooter
     */
    fun withLoadStateHeader(
        header: LoadStateAdapter<*>
    ): ConcatAdapter {
        addLoadStateListener { loadStates ->
            header.loadState = loadStates.prepend
        }
        return ConcatAdapter(header, this)
    }

    /**
     * Create a [ConcatAdapter] with the provided [LoadStateAdapter]s displaying the
     * [LoadType.APPEND] [LoadState] as a list item at the start of the presented list.
     *
     * @see LoadStateAdapter
     * @see withLoadStateHeaderAndFooter
     * @see withLoadStateHeader
     */
    fun withLoadStateFooter(
        footer: LoadStateAdapter<*>
    ): ConcatAdapter {
        addLoadStateListener { loadStates ->
            footer.loadState = loadStates.append
        }
        return ConcatAdapter(this, footer)
    }

    /**
     * Create a [ConcatAdapter] with the provided [LoadStateAdapter]s displaying the
     * [LoadType.PREPEND] and [LoadType.APPEND] [LoadState]s as list items at the start and end
     * respectively.
     *
     * @see LoadStateAdapter
     * @see withLoadStateHeader
     * @see withLoadStateFooter
     */
    fun withLoadStateHeaderAndFooter(
        header: LoadStateAdapter<*>,
        footer: LoadStateAdapter<*>
    ): ConcatAdapter {
        addLoadStateListener { loadStates ->
            header.loadState = loadStates.prepend
            footer.loadState = loadStates.append
        }
        return ConcatAdapter(header, this, footer)
    }


    /**
     * Note: [getItemId] is final, because stable IDs are unnecessary and therefore unsupported.
     *
     * [PagingDataAdapter]'s async diffing means that efficient change animations are handled for
     * you, without the performance drawbacks of [RecyclerView.Adapter.notifyDataSetChanged].
     * Instead, the diffCallback parameter of the [PagingDataAdapter] serves the same
     * functionality - informing the adapter and [RecyclerView] how items are changed and moved.
     */
    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        throw UnsupportedOperationException("Stable ids are unsupported on PagingDataAdapter.")
    }

}