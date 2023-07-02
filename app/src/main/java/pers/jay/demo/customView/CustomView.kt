package pers.jay.demo.customView

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class CustomView(val id: Int = 0, val cname: String = "") : Parcelable {

    PROGRESS_BAR(1, "进度条"),
    SCALE_RULER(2, "刻度尺"),
    BANNER(3, "轮播图"),
    DASHBOARD(4, "仪表盘"),
    BEZIER_RIPPLE(5, "贝塞尔水波纹")

}