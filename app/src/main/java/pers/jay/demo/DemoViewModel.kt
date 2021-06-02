package pers.jay.demo

import pers.jay.library.base.livedata.StateLiveData
import pers.jay.library.base.viewmodel.BaseFlowObserver
import pers.jay.library.base.viewmodel.BaseViewModel
import pers.jay.library.network.BaseResponse

class DemoViewModel : BaseViewModel<DemoRepo>() {

    fun test(): StateLiveData<List<Tab>> {
        return requestOnFlow(mRepo.testRequestWithFlow(), object : BaseFlowObserver<List<Tab>>() {

        })
    }
}