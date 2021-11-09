package pers.jay.library.ui.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
abstract class BaseRvAdapter<T, VH: BaseRvAdapter.BaseViewHolder>() : RecyclerView.Adapter<VH>() {

    private var mData = mutableListOf<T>()

    open fun getItem(@IntRange(from = 0) position: Int): T {
        return mData[position]
    }

    open fun getItemOrNull(@IntRange(from = 0) position: Int): T? {
        return mData.getOrNull(position)
    }

    open fun getItemPosition(item: T?): Int {
        return if (item != null && mData.isNotEmpty()) mData.indexOf(item) else -1
    }

    open fun setList(list: Collection<T>) {
        if (list.isEmpty()) {
            return
        }
        mData.clear()
        mData.addAll(list)
        notifyItemRangeChanged(0, list.size)
    }

    open fun addList(list: Collection<T>) {
        mData.addAll(list)
        notifyItemRangeInserted(mData.size, list.size)
    }

    open fun addList(@IntRange(from = 0) position: Int, list: Collection<T>) {
        mData.addAll(position, list)
        notifyItemRangeInserted(mData.size, list.size)
    }

    open fun addItem(item: T) {
        mData.add(item)
        val originalSize = mData.size
        notifyItemInserted(originalSize)
    }

    open fun addItem(@IntRange(from = 0) position: Int, data: T) {
        mData.add(position, data)
        notifyItemInserted(position)
    }

    open fun setItem(@IntRange(from = 0) position: Int, data: T) {
        if (position > mData.size) {
            return
        }
        mData[position] = data
        notifyItemChanged(position)
    }

    open fun removeAt(@IntRange(from = 0) position: Int) {
        mData.removeAt(position)
        notifyItemRemoved(position)
    }

    open fun remove(data: T) {
        val index = getItemPosition(data)
        if (index == -1) {
            return
        }
        removeAt(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater.from(parent.context).inflate(getItemLayoutResId(), parent, false)
        return BaseViewHolder(itemView) as VH
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        onBind(holder, item)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    abstract fun onBind(holder: VH, item: T)

    abstract fun getItemLayoutResId(): Int

    open class BaseViewHolder(root: View) : RecyclerView.ViewHolder(root) {

        val mContext = root.context

    }
}