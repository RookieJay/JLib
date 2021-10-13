package pers.jay.demo.paging

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import pers.jay.demo.data.Article
import pers.jay.library.base.viewmodel.BaseViewModel

class PagingViewModel : BaseViewModel<PagingModel>() {


    fun loadHomeArticles(): Flow<PagingData<Article>> {
        return mRepo.loadPagingData()
    }


}
