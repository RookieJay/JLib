package pers.jay.demo.viewbinding

import com.blankj.utilcode.util.LogUtils
import pers.jay.demo.data.Tab
import pers.jay.demo.net.Daily
import pers.jay.demo.net.HWeatherResponse
import pers.jay.demo.net.WanResponse
import pers.jay.library.base.livedata.StateLiveData
import pers.jay.library.base.viewmodel.BaseViewModel

class DemoViewModel : BaseViewModel<DemoRepo>() {

    fun test(): StateLiveData<List<Tab>> {
        return mRepo.testRequestWithFlow().requestData {
            onStart {
                LogUtils.i(TAG, "onStart")
            }
            onResult { LogUtils.i(TAG, "onResult") }
            onEmpty { LogUtils.i(TAG, "onEmpty") }
            onError { LogUtils.i(TAG, "onError") }
            onCompletion { LogUtils.i(TAG, "onCompletion") }
        }
    }

    fun test2(): StateLiveData<List<Daily>> {
        val liveData = createStateLiveData<List<Daily>>()
        return mRepo.testRequestWithFlow2().requestData(liveData) {
            onStart {
                LogUtils.i(TAG, "onStart")
            }
            onResult { LogUtils.i(TAG, "onResult") }
            onEmpty { LogUtils.i(TAG, "onEmpty") }
            onError { LogUtils.i(TAG, "onError") }
            onCompletion { LogUtils.i(TAG, "onCompletion") }
        }
    }

}