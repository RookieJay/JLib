package pers.jay.library.ui.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView


/**
 * @Author RookieJay
 * @Time 2021/11/9 16:15
 * @Description 通用RecyclerView适配器，封装了一些常规数据操作
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseRvAdapter<T, VH : BaseRvAdapter.BaseViewHolder>(
    private val getItemLayoutResId: () -> Int = { 0 },
    private val onBind: ((VH, T, Int) -> Unit)? = null
) :
    RecyclerView.Adapter<VH>() {

    protected val TAG = javaClass.simpleName

    private var mData = mutableListOf<T>()

    fun getData() = mData

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
        if (list.isNotEmpty()) {
            mData.clear()
        }
        mData.addAll(list)
        notifyItemInserted(0)
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

    open fun clear() {
        mData.clear()
        notifyItemRangeRemoved(0, mData.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView =
            LayoutInflater.from(parent.context).inflate(getItemLayoutResId(), parent, false)
        return BaseViewHolder(itemView) as VH
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        onBind?.invoke(holder, item, position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    open class BaseViewHolder(root: View) : RecyclerView.ViewHolder(root) {

        val mContext = root.context

    }
}