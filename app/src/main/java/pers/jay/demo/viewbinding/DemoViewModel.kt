package pers.jay.demo.viewbinding

import pers.jay.demo.data.Tab
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