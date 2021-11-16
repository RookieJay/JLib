package pers.jay.library.ui.rv

import androidx.recyclerview.widget.DiffUtil
import com.blankj.utilcode.util.ObjectUtils

open class DefaultDifferCallback<T: Any>: DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return ObjectUtils.equals(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return ObjectUtils.equals(oldItem, newItem)
    }
}