package pers.jay.demo

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Tab {
    /**
     * children : [{"children":[],"courseId":13,"id":60,"name":"Android Studio相关","order":1000,"parentChapterId":150,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":169,"name":"gradle","order":1001,"parentChapterId":150,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":269,"name":"官方发布","order":1002,"parentChapterId":150,"userControlSetTop":false,"visible":1}]
     * courseId : 13
     * id : 150
     * name : 开发环境
     * order : 1
     * parentChapterId : 0
     * userControlSetTop : false
     * visible : 1
     */
    var courseId = 0
    var id = 0
    var name: String? = null
    var order = 0
    var parentChapterId = 0
    var isUserControlSetTop = false
    var visible = 0
    var children: List<Tab?>? = null

    companion object {
        const val TYPE_PARENT = 1
        const val TYPE_CHILD = 2
    }

    override fun toString(): String {
        return "Tab(name=$name)"
    }


}