package pers.jay.demo.loadsir

import android.content.Context
import android.view.View

class RetryCallback: CustomCallback("加载失败，请点击重试") {

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        return false
    }

}