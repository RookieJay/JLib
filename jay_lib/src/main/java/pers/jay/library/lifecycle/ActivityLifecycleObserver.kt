package pers.jay.library.lifecycle

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import org.simple.eventbus.EventBus

/**
 * @Date: 2020/9/30 14:37
 * @Description: Activity生命周期日志记录器
 */
class ActivityLifecycleObserver(tag: String) : DefaultLifecycleObserver {

    private val TAG = tag

    override fun onCreate(owner: LifecycleOwner) {
        Log.i(TAG, "onCreate")
        EventBus.getDefault().register(owner)
    }

    override fun onStart(owner: LifecycleOwner) {
        Log.i(TAG, "onStart")
    }

    override fun onResume(owner: LifecycleOwner) {
        Log.i(TAG, "onResume")
    }

    override fun onPause(owner: LifecycleOwner) {
        Log.i(TAG, "onPause")
    }

    override fun onStop(owner: LifecycleOwner) {
        Log.i(TAG, "onStop")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Log.i(TAG, "onDestroy")
        EventBus.getDefault().unregister(owner)
    }
}