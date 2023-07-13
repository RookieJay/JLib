package pers.jay.library.ui.rv

import androidx.recyclerview.widget.RecyclerView

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

}