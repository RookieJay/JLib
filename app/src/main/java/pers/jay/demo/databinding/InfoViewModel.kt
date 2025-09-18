package pers.jay.demo.databinding

import pers.jay.demo.data.Tab
import pers.jay.library.base.livedata.StateLiveData
import pers.jay.library.base.viewmodel.BaseViewModel
import pers.jay.library.network.errorhandle.BussException

class InfoViewModel : BaseViewModel<InfoModel>() {

    /**
     * 此处处理数据，接口返回列表，处理成返回第一个
     */
    fun loadData(): StateLiveData<List<Tab>> {

        return mRepo.loadData().requestData {
            onStart {

            }
            onResult {

            }
            onEmpty {

            }
        }.apply {
            this.bussErrorHandle { response ->
                if (response.code == 0) {
                    return@bussErrorHandle BussException(message = "fe")
                }
                return@bussErrorHandle null
            }
        }
    }

}
