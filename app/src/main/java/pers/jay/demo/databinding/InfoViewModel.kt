package pers.jay.demo.databinding

import pers.jay.demo.data.Tab
import pers.jay.library.base.livedata.StateLiveData
import pers.jay.library.base.viewmodel.BaseViewModel

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
            onError {

            }
        }
    }

}
