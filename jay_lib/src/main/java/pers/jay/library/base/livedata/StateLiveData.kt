package pers.jay.library.base.livedata

import pers.jay.library.network.BaseResponse

/**
 * @Author RookieJay
 * @Time 2021/5/31 10:39
 * @Description 含有数据状态的LiveData，封装了[BaseResponse]
 */
class StateLiveData<T> : SingleLiveData<BaseResponse<T>>() {

}