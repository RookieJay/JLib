package pers.jay.demo.room

import androidx.lifecycle.LiveData
import androidx.room.*
import pers.jay.demo.data.Article

@Dao
interface ArticleDao {

    @Insert
    suspend fun insert(vararg articles: Article?): LongArray?

    @Delete
    suspend fun delete(article: Article): Int

    @Update
    suspend fun update(vararg articles: Article): Int

    @Query("select * from article t")
    fun articles(): List<Article>

    @get:Query("select * from article t")
    val articles: LiveData<List<Article>>
}