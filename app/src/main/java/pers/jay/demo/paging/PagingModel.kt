package pers.jay.demo.paging

import androidx.paging.*
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.flow.Flow
import pers.jay.demo.WanRepo
import pers.jay.demo.data.Article
import pers.jay.demo.data.ArticleInfo
import pers.jay.demo.room.DBManager
import pers.jay.library.base.ext.showToast

class PagingModel : WanRepo() {

    val articleDao = DBManager.getDB().articleDao()

    fun loadPagingData(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 5, enablePlaceholders = true, maxSize = 20),
            pagingSourceFactory = { ArticlePagingSource() }
        ).flow
    }

    /**
     * 泛型参数类型
     * 第一个类型表示页数的数据类型，我们没有特殊需求，所以直接用整型就可以了。
     * 第二个类型表示每一项数据（注意不是每一页）所对应的对象类型。
     */
    inner class ArticlePagingSource : PagingSource<Int, Article>() {

        override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
            return null
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
            return try {
                val page = params.key ?: 0 // set page 1 as default
                val pageSize = params.loadSize
                val repoResponse = wanService.homeArticles(page)
                val articleInfo = repoResponse.data as ArticleInfo
                val repoItems = articleInfo.datas as List<Article>
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (repoItems.isNotEmpty()) page + 1 else null
                if (page == 0) {
                    saveFirstPage(repoItems)
                    val dbData = getFirstPage()
                    LogUtils.d(TAG, "dbData:${dbData.size}")
                }
                LoadResult.Page(repoItems, prevKey, nextKey)
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("load error ")
                val page = params.key ?: 0
                if (page == 0) {
                    val localData = articleDao.articles()
                    LoadResult.Page(localData, null, 1)
                } else {
                    LoadResult.Error(e)
                }
            }
        }
    }

    private suspend fun saveFirstPage(repoItems: List<Article>) {
        val result = articleDao.insert(repoItems)
        LogUtils.d(TAG, "saveFirstPage result ${result.toString()}")
    }

    private suspend fun getFirstPage(): List<Article> {
        return articleDao.articles()
    }

}
