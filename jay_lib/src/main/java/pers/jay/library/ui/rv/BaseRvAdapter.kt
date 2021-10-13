package pers.jay.library.ui.rv

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRvAdapter : RecyclerView.Adapter<BaseRvAdapter.BaseViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        return BaseViewHolder(getItemView(parent, viewType))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    abstract fun getItemView(parent: ViewGroup, viewType: Int): View

    open class BaseViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    }
}