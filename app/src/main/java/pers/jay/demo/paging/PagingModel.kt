package pers.jay.demo.paging

import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import pers.jay.demo.WanRepo
import pers.jay.demo.data.Article
import pers.jay.demo.data.ArticleInfo
import pers.jay.library.base.ext.showMessage

class PagingModel : WanRepo() {

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
                val page = params.key ?: 1 // set page 1 as default
                val pageSize = params.loadSize
                val repoResponse = wanService.homeArticles(page)
                val articleInfo = repoResponse.data as ArticleInfo
                val repoItems = articleInfo.datas as List<Article>
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (repoItems.isNotEmpty()) page + 1 else null
                LoadResult.Page(repoItems, prevKey, nextKey)
            } catch (e: Exception) {
                showMessage("load error ")
                LoadResult.Error(e)
            }
        }
    }

}
