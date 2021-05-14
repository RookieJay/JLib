package pers.jay.library.utils

import com.blankj.utilcode.util.GsonUtils
import java.lang.reflect.Type

class JsonUtils {

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder= JsonUtils()
    }

    fun<T> getJson(json: String?, clazz: Class<T>): T {
       return GsonUtils.fromJson(json, clazz)
    }

    fun<T> getJson(json: String?, type: Type): T {
        return GsonUtils.fromJson(json, type)
    }
}