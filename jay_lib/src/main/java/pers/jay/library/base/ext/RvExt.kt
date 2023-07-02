package pers.jay.library.base.ext

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun <V: View> RecyclerView.ViewHolder.getView(@IdRes idRes: Int): V = this.itemView.findViewById(idRes)

/**
 * 当Item的高度如是固定的，设置这个属性为true可以提高性能，尤其是当RecyclerView有条目插入、删除时性能提升更明显。
 */
fun <A : RecyclerView.Adapter<out RecyclerView.ViewHolder>> RecyclerView.config(adapter: A) {
    setHasFixedSize(true)
    layoutManager = LinearLayoutManager(context)
    this.adapter = adapter
}