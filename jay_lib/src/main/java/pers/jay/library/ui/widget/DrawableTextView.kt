package pers.jay.library.ui.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import pers.jay.library.R

/**
 * 带有 drawable 的TextView
 * 统一处理了 drawable 的位置和点击事件
 */
class DrawableTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var hasLeftDrawable = false
    private var hasTopDrawable = false
    private var hasRightDrawable = false
    private var hasBotttomDrawable = false

    private var drawableClickListener: OnDrawableClickListener? = null


    public fun setDrawableClickListener(drawableClickListener: OnDrawableClickListener) {
        this.drawableClickListener = drawableClickListener
    }

    //从attrs中获取期望的drawable的宽高，资源，上下左右4个位置
    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val a: TypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView)
            val mWidth = a.getDimensionPixelSize(R.styleable.DrawableTextView_drawable_width, 0)
            val mHeight =
                a.getDimensionPixelSize(R.styleable.DrawableTextView_drawable_height, 0)
            val mDrawable = a.getDrawable(R.styleable.DrawableTextView_drawable_src)
            val mLocation = a.getInt(R.styleable.DrawableTextView_drawable_location, LEFT)
            a.recycle()
            drawDrawable(mDrawable, mWidth, mHeight, mLocation)
        }
    }

    private fun drawDrawable(mDrawable: Drawable?, mWidth: Int, mHeight: Int, mLocation: Int) {
        if (mDrawable != null) {
            if (mWidth != 0 && mHeight != 0) {
                mDrawable.setBounds(0, 0, mWidth, mHeight)
            }
            when (mLocation) {
                LEFT -> this.setCompoundDrawables(
                    mDrawable, null,
                    null, null
                )
                TOP -> this.setCompoundDrawables(
                    null, mDrawable,
                    null, null
                )
                RIGHT -> this.setCompoundDrawables(
                    null, null,
                    mDrawable, null
                )
                BOTTOM -> this.setCompoundDrawables(
                    null, null, null,
                    mDrawable
                )
            }
        }
    }

    override fun setCompoundDrawables(
        left: Drawable?,
        top: Drawable?,
        right: Drawable?,
        bottom: Drawable?
    ) {
        hasLeftDrawable = left != null
        hasTopDrawable = top != null
        hasRightDrawable = right != null
        hasBotttomDrawable = bottom != null
        super.setCompoundDrawables(left, top, right, bottom)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            performClick()
            if (drawableClickListener == null) {
                return super.onTouchEvent(event)
            }
            drawableClickListener?.let { listener ->
                if (hasLeftDrawable) {
                    val drawableLeft: Drawable = compoundDrawables[LEFT]
                    if (event.rawX <= left + drawableLeft.bounds.width()) {
                        listener.onLeft(drawableLeft)
                        return true
                    }
                }
                if (hasRightDrawable) {
                    val drawableRight: Drawable = compoundDrawables[RIGHT]
                    if (event.rawX >= right - drawableRight.bounds.width()) {
                        listener.onRight(drawableRight)
                        return true
                    }
                }
                if (hasTopDrawable) {
                    val drawableTop: Drawable = compoundDrawables[TOP]
                    if (event.rawY >= top + drawableTop.bounds.height()) {
                        listener.onTop(drawableTop)
                        return true
                    }
                }
                if (hasBotttomDrawable) {
                    val drawableBottom: Drawable = compoundDrawables[BOTTOM]
                    if (event.rawY <= bottom - drawableBottom.bounds.height()) {
                        listener.onBottom(drawableBottom)
                        return true
                    }
                }
            }

        }
        return true
    }

    companion object {
        //设置方向
        private const val LEFT = 0
        private const val TOP = 1
        private const val RIGHT = 2
        private const val BOTTOM = 3
    }

    interface OnDrawableClickListener {
        fun onLeft(left: Drawable) {}
        fun onTop(top: Drawable) {}
        fun onRight(right: Drawable) {}
        fun onBottom(bottom: Drawable) {}
    }
}