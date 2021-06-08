package pers.jay.demo.databinding

import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.zip
import pers.jay.demo.Tab
import pers.jay.library.base.livedata.StateLiveData
import pers.jay.library.base.repository.DataState
import pers.jay.library.base.viewmodel.BaseViewModel
import pers.jay.library.network.BaseResponse

class InfoViewModel : BaseViewModel<InfoModel>() {

    /**
     * 此处处理数据，接口返回列表，处理成返回第一个
     */
    fun loadData(): StateLiveData<Tab> {
        val tabLiveData = createStateLiveData<Tab>()
        val response = BaseResponse<Tab>()
//        requestOnFlow(mRepo.loadData(), object : BaseStateObserver<List<Tab>>() {
//
//            override fun onStart(response: BaseResponse<List<Tab>>) {
//                super.onStart(response)
//                res.dataState = response.dataState
//            }
//
//            override fun onCatch(response: BaseResponse<List<Tab>>, e: Throwable) {
//                super.onCatch(response, e)
//                res.dataState = response.dataState
//            }
//
//            override fun onError(msg: String) {
//                super.onError(msg)
//
//            }
//
//            override fun onCompletion() {
//                super.onCompletion()
//            }
//
//            override fun onSuccess(response: BaseResponse<List<Tab>>) {
//                super.onSuccess(response)
//
//                res.dataState = DataState.STATE_SUCCESS
//                res.data = response.data?.get(0)
//                tabLiveData.postValue(res)
//            }
//        })

        // 合并请求演示
        launchOnIO {
            val flow1 = mRepo.loadData()
            val flow2 = mRepo.loadData()
            flow1.zip(flow2, transform = { t1, t2 ->
                val data1 = t1.data?.toMutableList()
                val data2 = t2.data?.toMutableList()
                data1?.apply {
                    data2?.apply {
                        LogUtils.d(TAG, "data1 size:", data1.size)
                        LogUtils.d(TAG, "data2 size:", data2.size)
                        data1.addAll(data2)
                    }
                }
                data1
            })
                .flowOn(Dispatchers.Main)
                .collect {
                    it?.apply {
                        LogUtils.d(TAG, "data size:", it.size)
                        LogUtils.d(it)
                        response.dataState = DataState.STATE_SUCCESS
                        response.data = it[1]
                        tabLiveData.postValue(response)
                    }
                }
        }

        return tabLiveData
    }

}
