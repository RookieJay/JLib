package pers.jay.library.ui.rv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import pers.jay.library.utils.ViewBindingUtils
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class BaseVBAdapter<T, VH: BaseRvAdapter.BaseViewHolder, VB : ViewDataBinding> : BaseRvAdapter<T, VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        val vbClazz = ViewBindingUtils.getInstancedGenericClass(javaClass)
        vbClazz?.apply {
            val method = vbClazz.getMethod("inflate", LayoutInflater::class.java)
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = method.invoke(null, layoutInflater) as VB
            return BaseVBViewHolder(binding) as VH
        }
        throw RuntimeException("can't find matched ViewBinding")
    }

    override fun getItemView(parent: ViewGroup, viewType: Int): View {
        val binding = getVBWithReflect(parent.context)?: throw RuntimeException("can't find matched ViewBinding")
        return binding.root
    }

    /**
     * 反射获取当前类第一个泛型参数指定得ViewBinding
     */
    fun getVBWithReflect(context: Context): VB? {
        var binding: VB? = null
        val type = javaClass.genericSuperclass
        // ParameterizedType 参数化类型 声明类型中带有“<>”的都是参数化类型
        if (type is ParameterizedType) {
            // 获取第二个泛型参数类型
            val clazz = type.actualTypeArguments[1] as Class<VB>
            val method = clazz.getMethod("inflate", LayoutInflater::class.java)
            val layoutInflater = LayoutInflater.from(context)
            binding = method.invoke(null, layoutInflater) as VB
        }
        return binding
    }

    private fun getInstancedGenericKClass(z: Class<*>): Class<*>? {
        try {
            val type = z.genericSuperclass
            if (type is ParameterizedType) {
                val types = type.actualTypeArguments
                for (temp in types) {
                    if (temp is Class<*>) {
                        if (BaseViewHolder::class.java.isAssignableFrom(temp)) {
                            return temp
                        }
                    } else if (temp is ParameterizedType) {
                        val rawType = temp.rawType
                        if (rawType is Class<*> && BaseViewHolder::class.java.isAssignableFrom(rawType)) {
                            return rawType
                        }
                    }
                }
            }
        } catch (e: java.lang.reflect.GenericSignatureFormatError) {
            e.printStackTrace()
        } catch (e: TypeNotPresentException) {
            e.printStackTrace()
        } catch (e: java.lang.reflect.MalformedParameterizedTypeException) {
            e.printStackTrace()
        }
        return null
    }

    inner class BaseVBViewHolder(mBinding: VB) : BaseViewHolder(mBinding.root) {
        private val binding = ViewBindingUtils.getInstancedGenericClass(javaClass)
    }

    override fun onBind(holder: VH, item: T) {

    }


}