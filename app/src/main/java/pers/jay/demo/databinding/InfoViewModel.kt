package pers.jay.demo.databinding

import pers.jay.demo.Tab
import pers.jay.library.base.livedata.SingleLiveData
import pers.jay.library.base.viewmodel.BaseStateObserver
import pers.jay.library.base.viewmodel.BaseViewModel
import pers.jay.library.network.BaseResponse

class InfoViewModel: BaseViewModel<InfoModel>() {

    /**
     * 此处处理数据，接口返回列表，处理成返回第一个
     */
    fun loadData(): SingleLiveData<Tab> {
        val tabLiveData = createSingleLiveEvent<Tab>();
        requestOnFlow(mRepo.loadData(), object : BaseStateObserver<List<Tab>>() {
            override fun onSuccess(response: BaseResponse<List<Tab>>) {
                super.onSuccess(response)
                val list = response.data
                tabLiveData.postValue(list?.get(0))
            }
        })
        return tabLiveData
    }

}
