package pers.jay.demo.vm

import pers.jay.demo.DemoRepo
import pers.jay.demo.Tab
import pers.jay.library.base.livedata.StateLiveData
import pers.jay.library.base.viewmodel.BaseViewModel

class DemoViewModel : BaseViewModel<DemoRepo>() {

    fun test(): StateLiveData<List<Tab>> {
        return requestWithFlow(mRepo.testRequestWithFlow()) {
            onStart {  }
            onSuccess {  }
            onEmpty {  }
            onError {  }
            onCompletion {  }
        }
    }

}