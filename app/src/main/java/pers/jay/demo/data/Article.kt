package pers.jay.demo.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize

@Entity
@TypeConverters(TagConverter::class)
@Parcelize
data class Article(
    var apkLink: String? = null,
    var audit: Int? = null,
    var author: String? = null,
    var canEdit: Boolean? = null,
    var chapterId: Int? = null,
    var chapterName: String? = null,
    var collect: Boolean? = null,
    var courseId: Int? = null,
    var desc: String? = null,
    var descMd: String? = null,
    var envelopePic: String? = null,
    var fresh: Boolean? = null,
    var host: String? = null,
    @PrimaryKey
    var id: Int? = null,
    var link: String? = null,
    var niceDate: String? = null,
    var niceShareDate: String? = null,
    var origin: String? = null,
    var prefix: String? = null,
    var projectLink: String? = null,
    var publishTime: Long? = null,
    var realSuperChapterId: Int? = null,
    var selfVisible: Int? = null,
    var shareDate: Long? = null,
    var shareUser: String? = null,
    var superChapterId: Int? = null,
    var superChapterName: String? = null,
    var tags: List<Tag>? = null,
    var title: String? = null,
    var type: Int? = null,
    var userId: Int? = null,
    var visible: Int? = null,
    var zan: Int? = null
) : Parcelable

@Parcelize
data class Tag(
    var name: String? = null,
    var url: String? = null
) : Parcelable

class TagConverter {

    private val gson by lazy {
        GsonUtils.getGson()
    }

    @TypeConverter
    fun string2Obj(json: String) : List<Tag> {
        val tagListType = object : TypeToken<List<Tag>>(){}.type
        return gson.fromJson(json, tagListType)
    }

    @TypeConverter
    fun obj2String(tag: List<Tag>) : String {
        return gson.toJson(tag)
    }
}