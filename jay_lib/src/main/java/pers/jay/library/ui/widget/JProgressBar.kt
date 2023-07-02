package pers.jay.library.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import com.blankj.utilcode.util.LogUtils
import pers.jay.library.R
import pers.jay.library.base.ext.dp2px
import pers.jay.library.base.ext.getColor

class  JProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val TYPE_CIRCLE = 1
        const val TYPE_LINE = 2
        const val TYPE_FADE_LINE = 3
    }

    /**
     * 是否支持渐隐
     */
    var fadeMode = false

    private val mPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    /**
     * 画笔宽度
     */
    private var mStrokeWidth: Int = dp2px(5f).toInt()

    /**
     * 绘制颜色
     */
    private var mColor: Int = getColor(R.color.green)

    /**
     * 进度条类型，默认圆形
     */
    private var mShape: Int = TYPE_CIRCLE

    /**
     * 进度：0-100
     */
    private var mProgress: Float = 100f

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.JProgressBar)
        mColor = array.getColor(R.styleable.JProgressBar_color, mColor)
        mShape = array.getInt(R.styleable.JProgressBar_shape, mShape)
        mStrokeWidth = array.getDimensionPixelSize(R.styleable.JProgressBar_stroke, mStrokeWidth)
        mProgress = array.getFloat(R.styleable.JProgressBar_progress, mProgress)
        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 先执行原测量算法
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 获取原来的测量结果
//        getMeasuredWidth();
//        getMeasuredHeight();
        // 利用原来的测量结果计算新的尺寸
        val width: Int = measureView(widthMeasureSpec)
        val height: Int = measureView(heightMeasureSpec)

        // 保存计算后的结果
        setMeasuredDimension(width, height)
    }

    /**
     * 测量就是为了告诉系统我这个控件该画多大，如果是给了确定的值比如设置的是Match_parent或者特定的dp值就很简单了，
     * 即按照measureSpec给出的大小返回就行，如果设置的是wrap_content，系统本身是不知道你的控件内部元素到底有多大的，
     * 所以就需要计算出一个最小值告诉给系统
     *
     * 如上述代码所示，如果判断得到设置的模式是MeasureSpec.EXACTLY，就把MeasureSpec中的尺寸值返回就行，
     * 如果判断得到设置的模式是MeasureSpec.AT_MOST，也就是代码中设置的 wrap_content，取最小的一个返回，
     * 最后调用setMeasuredDimension方法，传入处理后的长宽值。
     *
     * specMode一共有三种类型，如下所示：
     * 1. EXACTLY
     * 表示父视图希望子视图的大小应该是由specSize的值来决定的，系统默认会按照这个规则来设置子视图的大小，简单的说（当设置width或height为match_parent时，模式为EXACTLY，因为子view会占据剩余容器的空间，所以它大小是确定的）
     * 2. AT_MOST
     * 表示子视图最多只能是specSize中指定的大小。（当设置为 wrap_content 时，模式为AT_MOST, 表示子view的大小最多是多少，这样子view会根据这个上限来设置自己的尺寸）
     * 3. UNSPECIFIED
     * 表示开发人员可以将视图按照自己的意愿设置成任意的大小，没有任何限制。这种情况比较少见，不太会用到。
     */
    private fun measureView(measureSpec: Int): Int {
        var result: Float
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            // MATCH_PARENT
            result = specSize.toFloat()
        } else {
            result = dp2px(5f)
            if (specMode == MeasureSpec.AT_MOST) {
                // WRAP_CONTENT
                result = result.coerceAtMost(specSize.toFloat())
            }
        }
        return result.toInt()
    }

    override fun layout(l: Int, t: Int, r: Int, b: Int) {
        super.layout(l, t, r, b)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w
        mHeight = h
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun draw(canvas: Canvas) {
        LogUtils.d("drawProgress", "$mColor")
        mPaint.color = mColor
        mPaint.strokeWidth = mStrokeWidth.toFloat()
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeCap = Paint.Cap.ROUND
        canvas.drawLine(0f, 0f, (mProgress * mWidth) / 100, 0f, mPaint)
        super.draw(canvas)
    }

    fun setProgress(progress: Float) {
        if (progress < 0f || progress > 100f) {
            return
        }
        mProgress = progress
        // 重绘
        invalidate()
    }

    class Builder(val context: Context) {

        private var progressBar: JProgressBar = JProgressBar(context)

        fun color(@ColorRes color: Int): Builder {
            progressBar.mColor = progressBar.getColor(color)
            return this
        }

        fun shape(shape: Int): Builder {
            progressBar.mShape = shape
            return this
        }

        fun strokeWidth(stokeWidth: Int): Builder {
            progressBar.mStrokeWidth = stokeWidth
            return this
        }

        fun build(): JProgressBar {
            return progressBar
        }
    }
}