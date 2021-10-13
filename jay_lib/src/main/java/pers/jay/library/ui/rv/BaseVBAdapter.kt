package pers.jay.library.ui.rv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class BaseVBAdapter<VB : ViewDataBinding> : BaseRvAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVBViewHolder {
        val binding = getVBWithReflect(parent.context)
            ?: throw RuntimeException("can't find matched ViewBinding")
        return BaseVBViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    /**
     * 反射获取当前类第一个泛型参数指定得ViewBinding
     */
    fun getVBWithReflect(context: Context): VB? {
        var binding: VB? = null
        val type = javaClass.genericSuperclass
        // ParameterizedType 参数化类型 声明类型中带有“<>”的都是参数化类型
        if (type is ParameterizedType) {
            val clazz = type.actualTypeArguments[0] as Class<VB>
            val method = clazz.getMethod("inflate", LayoutInflater::class.java)
            val layoutInflater = LayoutInflater.from(context)
            binding = method.invoke(null, layoutInflater) as VB
        }
        return binding
    }

    inner class BaseVBViewHolder(mBinding: VB) : BaseRvAdapter.BaseViewHolder(mBinding.root) {

    }


}