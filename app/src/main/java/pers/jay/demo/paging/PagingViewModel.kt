package pers.jay.demo.paging

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import pers.jay.demo.data.Article
import pers.jay.library.base.viewmodel.BaseViewModel

class PagingViewModel : BaseViewModel<PagingModel>() {


    fun loadHomeArticles(): Flow<PagingData<Article>> {
        //cachedIn(viewModelScope),用于将数据在viewModelScope这个作用域内进行缓存。
        // 这样当页面发生旋转时重新创建时，就会直接读取缓存。
        return mRepo.loadPagingData()
//            .cachedIn(viewModelScope)
    }


}
