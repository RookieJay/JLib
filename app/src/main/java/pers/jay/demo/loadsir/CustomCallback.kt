package pers.jay.demo.loadsir

import android.content.Context
import android.view.View
import android.widget.TextView
import com.kingja.loadsir.callback.Callback
import pers.jay.demo.R

/**
 * @Author RookieJay
 * @Time 2021/5/31 13:29
 * @Description 最简单的Callback 只有一个文本信息，根据传入不同的信息区分
 */
open class CustomCallback(private val msg: String): Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_custom
    }

    override fun onViewCreate(context: Context?, view: View?) {
        super.onViewCreate(context, view)
        val text = view as TextView
        text.text = msg
    }

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        return true;
    }
}