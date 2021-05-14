package pers.jay.library.utils

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Asset文件操作工具类
 */
object AssetUtils {

    val instance by lazy { this }

    /**
     * 得到json文件中的内容
     *
     * @param context  上下文
     * @param fileName 文件名称
     */
    fun getJson(context: Context, fileName: String?): String? {
        val stringBuilder = StringBuilder()
        //获得assets资源管理器
        val assetManager = context.assets
        //使用IO流读取json文件内容
        try {
            val bufferedReader = BufferedReader(
                InputStreamReader(
                    assetManager.open(fileName!!), "utf-8"
                )
            )
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            bufferedReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }


}