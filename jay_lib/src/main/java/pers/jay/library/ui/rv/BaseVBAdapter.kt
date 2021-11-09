package pers.jay.library.ui.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import pers.jay.library.utils.ViewBindingUtils

/**
 * @Author RookieJay
 * @Time 2021/11/9 16:16
 * @Description 基于ViewBinding的RecyclerView适配器
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseVBAdapter<T, VH: BaseRvAdapter.BaseViewHolder, VB : ViewBinding> : BaseRvAdapter<T, VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val vbClazz = ViewBindingUtils.getInstancedGenericClass(javaClass)
        vbClazz?.apply {
            val method = getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = method.invoke(null, layoutInflater, parent, false) as VB
            return BaseVBViewHolder(binding) as VH
        }
        throw RuntimeException("can't find matched ViewBinding")
    }

    override fun getItemLayoutResId(): Int {
        return 0
    }

    override fun onBind(holder: VH, item: T) {
        val vbHolder = holder as BaseVBViewHolder<VB>
        onBind(vbHolder.binding, item)
    }

    abstract fun onBind(binding: VB, item: T)

    open class BaseVBViewHolder<VB: ViewBinding>(val binding: VB) : BaseViewHolder(binding.root) {

    }

}