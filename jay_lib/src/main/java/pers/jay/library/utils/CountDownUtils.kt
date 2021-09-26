package pers.jay.library.utils

import android.os.Handler
import android.os.Looper
import com.blankj.utilcode.util.LogUtils

class CountDownUtils(val total : Int) {

    private var mHandler: Handler = Handler(Looper.getMainLooper())
    // 总时长，单位：秒
    private var mTotal = total

    private lateinit var mListener : (Int) -> Unit // 声明 mListener 是一个函数（单方法接口）,入参Int，无返回值

    public fun start() {
        mHandler.postDelayed(mRunnable, 1000L)
    }

    private val mRunnable : Runnable = object : Runnable {
        override fun run() {
            if (mTotal > 0) {
                mListener.invoke(mTotal)
                mHandler.postDelayed(this, 1000L)
            } else {
                LogUtils.e("倒计时结束 $mTotal")
            }
            mTotal--
        }
    }

    fun setListener(listener : (Int) -> Unit) {
        this.mListener = listener
    }



}