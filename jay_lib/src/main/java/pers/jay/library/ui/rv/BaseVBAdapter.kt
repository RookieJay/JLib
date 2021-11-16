package pers.jay.library.ui.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.LogUtils
import kotlin.reflect.KClass

/**
 * @Author RookieJay
 * @Time 2021/11/9 16:16
 * @Description 基于ViewBinding的RecyclerView适配器
 */
@Suppress("UNCHECKED_CAST")
open class BaseVBAdapter<T, VB : ViewBinding>(
    private val vbClass: KClass<VB>,
    private val onBind: (VB, T, Int) -> Unit
) : BaseRvAdapter<T, BaseVBAdapter.BaseVBViewHolder<VB>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVBViewHolder<VB> {
        val start = System.currentTimeMillis()
//        val vbClazz = ViewBindingUtils.getVBClass(javaClass)
        val vbClazz = vbClass.java
        vbClazz.apply {
            // 调用ViewBinding实例的inflate方法
            val method = getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            )
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = method.invoke(null, layoutInflater, parent, false) as VB
            LogUtils.d(TAG, "onCreateViewHolder 耗时：${System.currentTimeMillis() - start}")
            return BaseVBViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: BaseVBViewHolder<VB>, position: Int) {
        val item = getItem(position)
        onBind.invoke(holder.binding, item, position)
    }

    open class BaseVBViewHolder<VB : ViewBinding>(val binding: VB) : BaseViewHolder(binding.root) {

    }

}