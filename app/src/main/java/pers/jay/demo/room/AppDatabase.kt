package pers.jay.demo.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pers.jay.demo.data.Article

@Database(entities = [Article::class], version = 1)
abstract class AppDatabase(applicationContext: Context) : RoomDatabase() {

    val db = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "jlib.sqlite.db"
    )
//        .allowMainThreadQueries() // 默认不允许在主线程操作，此处可开启，但不推荐
        .build()

    abstract fun articleDao(): ArticleDao

}