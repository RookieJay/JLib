package pers.jay.library.base.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * @Author RookieJay
 * @Time 2021/5/27 13:42
 * @Description model层：repository基类，用于数据储存及获取
 */
abstract class BaseRepository {

    /**
     * @desc   通过flow来处理请求，返回一个Flow对象交由ViewModel处理
     * @param  requestBlock 请求方法体
     * @return Unit
     */
    fun <T> requestOnFlow(requestBlock: suspend () -> T): Flow<T> {
        return flow<T> {
            val response = requestBlock.invoke()
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

}