package pers.jay.library.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.LogUtils

object UiUtils {

    /**
     * 判断触摸点是否在View上
     * @param view View
     * @param x 触摸点在屏幕上的横坐标
     * @param y 触摸点在屏幕上的纵坐标
     *
     * 注：
     * ev.getX()表示相对于控件自身左上角的X坐标
     * ev.getRawX()：表示相对于手机屏幕左上角的X坐标
     * Y坐标向下增大
     */
    fun isTouchInsideView(view: View, x: Int, y: Int): Boolean {
        val location = IntArray(2)
        // 这里是把left和top坐标放入数组
        view.getLocationOnScreen(location)
        val left = location[0]
        val top = location[1]
        val right = left + view.measuredWidth
        val bottom = top + view.measuredHeight
        return x in left..right && y in top..bottom
    }

    fun sp2Px(sp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, sp,
            Resources.getSystem().displayMetrics
        )
    }

    fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            Resources.getSystem().displayMetrics
        )
    }

    /**
     * 跑马灯
     */
    fun setMarquee(view: TextView) {
        view.isSelected = true
        view.isFocusable = true
        view.isFocusableInTouchMode = true
    }

    /**
     * RenderScript高斯模糊
     * Note：缩小的系数应该为2的整数次幂 ，即上面代码中的scale应该为1/2、1/4、1/8 ...
     * 参考 BitmapFactory.Options 对图片缩放 的inSample系数。据前辈们经验，一般scale = 1/8 为佳。
     */
    fun rsBlur(context: Context?, source: Bitmap, radius: Int, scale: Float): Bitmap? {
        LogUtils.d("UiUtils " + "origin size:" + source.width + "*" + source.height)
        val width = Math.round(source.width * scale)
        val height = Math.round(source.height * scale)
        val inputBmp = Bitmap.createScaledBitmap(source, width, height, false)
        val renderScript = RenderScript.create(context)
        LogUtils.i("UiUtils " + "scale size:" + inputBmp.width + "*" + inputBmp.height)
        // Allocate memory for Renderscript to work with
        val input = Allocation.createFromBitmap(renderScript, inputBmp)
        val output = Allocation.createTyped(renderScript, input.type)
        // Load up an instance of the specific script that we want to use.
        val scriptIntrinsicBlur = ScriptIntrinsicBlur.create(
            renderScript,
            Element.U8_4(renderScript)
        )
        scriptIntrinsicBlur.setInput(input)
        // Set the blur radius
        // ** 设置模糊半径**：设置一个模糊的半径,其值为 0－25
        scriptIntrinsicBlur.setRadius(radius.toFloat())
        // Start the ScriptIntrinisicBlur
        scriptIntrinsicBlur.forEach(output)
        // Copy the output to the blurred bitmap
        output.copyTo(inputBmp)
        renderScript.destroy()
        return inputBmp
    }
}