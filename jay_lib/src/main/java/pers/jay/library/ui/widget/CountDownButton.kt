package pers.jay.library.ui.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.LogUtils
import pers.jay.library.R
import pers.jay.library.base.ext.getColor
import pers.jay.library.base.ext.showToast

class CountDownButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    constructor(builder: Builder) : this(builder.context) {
        countDownDuration = builder.countDownDuration
        normalText = builder.normalText
        timerText = builder.timerText
        endText = builder.endText
        normalTextColor = getColor(builder.normalTextColor)
        timerTextColor = getColor(builder.timerTextColor)
        endTextColor = getColor(builder.endTextColor)
        normalTextBg = builder.normalTextBg
//        timerTextBg = getColor(builder.timerTextBg)
//        endTextBg = getColor(builder.endTextBg)
    }

    companion object {
        private val TAG = CountDownButton::class.simpleName

        inline fun build(context: Context, block: Builder.() -> Unit): CountDownButton {
            return Builder(context).apply(block).build()
        }
    }

    private lateinit var countDownTimer: CountDownTimer

    private var mStatus = State.NORMAL

    private var countDownDuration = 120
    private var normalText = ""
    private var timerText = ""
    private var endText = ""
    private var normalTextColor = getColor(R.color.white)
    private var timerTextColor = getColor(R.color.gray)
    private var endTextColor = getColor(R.color.white)
    private var normalTextBg = getColor(R.color.white)
    private var timerTextBg = getColor(R.color.white)
    private var endTextBg = getColor(R.color.blue_dark)

    private lateinit var mOnTick: (Int) -> Unit

    init {
        initAttrs(attrs)
        initTimer()
    }

    fun setOnTickListener(onTick: (Int) -> Unit) {
        mOnTick = onTick
    }

    private fun initAttrs(attrs: AttributeSet?) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownButton)
        countDownDuration =
            typeArray.getInt(R.styleable.CountDownButton_duration, countDownDuration)
        normalText = typeArray.getString(R.styleable.CountDownButton_normal_text).toString()
        timerText = typeArray.getString(R.styleable.CountDownButton_timer_text).toString()
        endText = typeArray.getString(R.styleable.CountDownButton_end_text).toString()
        normalTextColor =
            typeArray.getColor(R.styleable.CountDownButton_normal_text_color, normalTextColor)
        timerTextColor =
            typeArray.getColor(R.styleable.CountDownButton_timer_text_color, timerTextColor)
        endTextColor = typeArray.getColor(R.styleable.CountDownButton_end_text_color, endTextColor)
        normalTextBg = typeArray.getResourceId(R.styleable.CountDownButton_normal_bg, normalTextBg)
        timerTextBg = typeArray.getResourceId(R.styleable.CountDownButton_timer_bg, timerTextBg)
        endTextBg = typeArray.getResourceId(R.styleable.CountDownButton_end_bg, endTextBg)
        typeArray.recycle()
        notifyStatus(State.NORMAL)
    }

    private fun initTimer() {
        countDownTimer = object : CountDownTimer(countDownDuration * 1000L, 1000L) {

            override fun onTick(millisUntilFinished: Long) {
                LogUtils.d(TAG, "millisUntilFinished: $millisUntilFinished")
                mOnTick.invoke((millisUntilFinished / 1000).toInt())
            }

            override fun onFinish() {
                notifyStatus(State.END)
                countDownTimer.cancel()
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(event)
    }


    override fun performClick(): Boolean {
        showToast("performClick")
        if (mStatus == State.COUNTING) {
            return true
        }
        notifyStatus(State.COUNTING)
        countDownTimer.start()
        return super.performClick()
    }

    private fun notifyStatus(state: State) {
        mStatus = state
        when (mStatus) {
            State.NORMAL -> {
                text = normalText
//                background = ContextCompat.getDrawable(context, normalTextBg)
            }
            State.COUNTING -> {
                text = timerText
            }
            State.END -> {
                text = endText
            }
        }
    }

    class Builder(val context: Context) {

        var countDownDuration = 120
            private set
        var normalText = ""
            private set
        var timerText = ""
            private set
        var endText = ""
            private set
        var normalTextColor = ContextCompat.getColor(context, R.color.white)
            private set
        var timerTextColor = ContextCompat.getColor(context, R.color.gray)
            private set
        var endTextColor = ContextCompat.getColor(context, R.color.white)
            private set
        var normalTextBg = ContextCompat.getColor(context, R.color.blue_dark)
            private set
        var timerTextBg = ContextCompat.getColor(context, R.color.white)
            private set
        var endTextBg = ContextCompat.getColor(context, R.color.blue_dark)
            private set


        fun build() = CountDownButton(this)

        fun countDownDuration(duration: Int) {
            this.countDownDuration = duration
        }

        fun normalText(normalText: String): Builder {
            this.normalText = normalText
            return this
        }

        fun timerText(timerText: String): Builder {
            this.timerText = timerText
            return this
        }

        fun endText(endText: String): Builder {
            this.endText = endText
            return this
        }

        fun normalTextColor(@ColorRes resId: Int): Builder {
            this.normalTextColor = resId
            return this
        }

        fun timerTextColor(@ColorRes resId: Int): Builder {
            this.timerTextColor = resId
            return this
        }

        fun endTextColor(@ColorRes resId: Int): Builder {
            this.endTextColor = resId
            return this
        }

        fun normalTextBg(@DrawableRes @ColorRes resId: Int): Builder {
            this.normalTextBg = resId
            return this
        }

        fun timerTextBg(@DrawableRes @ColorRes resId: Int): Builder {
            this.timerTextBg = resId
            return this
        }

        fun endTextBg(@DrawableRes @ColorRes resId: Int): Builder {
            this.endTextBg = resId
            return this
        }

    }

    enum class State {
        NORMAL(), COUNTING(), END()
    }


}