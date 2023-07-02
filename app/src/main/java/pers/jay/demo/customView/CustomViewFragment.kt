package pers.jay.demo.customView

import android.animation.ValueAnimator
import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import pers.jay.demo.R
import pers.jay.demo.databinding.FragmentCustomViewBinding
import pers.jay.library.base.ext.singleClick
import pers.jay.library.base.viewbinding.BaseVBFragment
import pers.jay.library.ui.widget.CountDownButton
import pers.jay.library.ui.widget.JProgressBar

class CustomViewFragment : BaseVBFragment<FragmentCustomViewBinding>() {

    companion object {
        const val VIEW_TYPE = "view_type"
    }

    private var customView: CustomView? = null

    override fun initParams(bundle: Bundle?) {
        super.initParams(bundle)
        customView = bundle?.getParcelable(VIEW_TYPE)
        LogUtils.d(TAG, "data ${customView.toString()}")
    }

    override fun initView(savedInstanceState: Bundle?) {
        initTitle()
        customView?.apply {
            mBinding.start.singleClick {
                createProgressBar()
            }
            when (this) {
                CustomView.PROGRESS_BAR -> {
                    createProgressBar()
                }
                CustomView.SCALE_RULER -> {
                    createScaleRuler()
                }
                CustomView.BANNER -> {

                }
                CustomView.DASHBOARD -> {

                }
                CustomView.BEZIER_RIPPLE -> {

                }
            }
        }
    }

    private fun createScaleRuler() {
        val cdButton = CountDownButton.build(context) {
            countDownDuration(30)
            normalText("获取验证码")
            normalTextColor(R.color.white)
            timerText("重新获取(%s)")
            timerTextColor(R.color.white)
            endText("重新获取")
            endTextColor(R.color.white)
        }
        cdButton.setOnTickListener {
            cdButton.text = "重新获取($it)"
        }
        mBinding.llCustom.addView(cdButton)
    }

    private fun initTitle() {
        mBinding.tvCustomViewTitle.text = customView?.cname
    }

    private fun createProgressBar() {
        val progressBar = JProgressBar.Builder(context)
            .color(R.color.red)
            .shape(JProgressBar.TYPE_LINE)
            .strokeWidth(20)
            .build()
        mBinding.llCustom.addView(progressBar)
        val animator = ValueAnimator.ofInt(0, 10).setDuration(5 * 1000L)
        animator.apply {
            addUpdateListener {
                val value = it.animatedValue as Int
                progressBar.setProgress(value.toFloat() * 10)
            }
            start()
        }

    }

    override fun initData(savedInstanceState: Bundle?) {

    }

}