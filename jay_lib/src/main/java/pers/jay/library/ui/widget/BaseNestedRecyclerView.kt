package pers.jay.library.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * 基于外部拦截法，处理RecyclerView的滑动冲突的基类
 */
abstract class BaseNestedRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    protected var lastX: Float = 0.0f
    protected var lastY: Float = 0.0f

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        var intercepted = false
        event?.let {
            val x = it.x
            val y = it.y
            when(it.action) {
                MotionEvent.ACTION_DOWN -> {
                    intercepted = false
                    lastX = it.x
                    lastY = it.y
                }
                MotionEvent.ACTION_MOVE -> {
                    if ((needInterceptEvent(x, y))) {
                        intercepted = true
                    }
                }
                MotionEvent.ACTION_UP -> {
                    intercepted = false
                }
                MotionEvent.ACTION_CANCEL -> {
                    intercepted = false
                }
            }
        }
        return intercepted
    }

    /**
     * 是否需要拦截事件
     * @param x 移动时的x坐标
     * @param y 移动时的y坐标
     */
    abstract fun needInterceptEvent(x: Float, y: Float): Boolean
}