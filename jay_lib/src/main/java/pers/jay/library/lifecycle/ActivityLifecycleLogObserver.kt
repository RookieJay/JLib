package pers.jay.library.lifecycle

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import org.simple.eventbus.EventBus

/**
 * @Date: 2020/9/30 14:37
 * @Description: Activity生命周期日志记录器
 */
class ActivityLifecycleLogObserver(private val TAG: String) : DefaultLifecycleObserver {


    override fun onCreate(owner: LifecycleOwner) {
        Log.d(TAG, "onCreate")
    }

    override fun onStart(owner: LifecycleOwner) {
        Log.d(TAG, "onStart")
    }

    override fun onResume(owner: LifecycleOwner) {
        Log.d(TAG, "onResume")
    }

    override fun onPause(owner: LifecycleOwner) {
        Log.d(TAG, "onPause")
    }

    override fun onStop(owner: LifecycleOwner) {
        Log.d(TAG, "onStop")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Log.d(TAG, "onDestroy")
    }
}