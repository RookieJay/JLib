package pers.jay.demo.viewbinding

import kotlinx.coroutines.flow.Flow
import pers.jay.demo.data.Tab
import pers.jay.demo.net.HWeatherResponse
import pers.jay.demo.net.TestClient
import pers.jay.demo.net.WanHttpClient
import pers.jay.demo.net.WanResponse
import pers.jay.demo.net.WanService
import pers.jay.library.base.repository.BaseRepository

class DemoRepo : BaseRepository() {

    fun testRequestWithFlow(): Flow<WanResponse<List<Tab>>> {
        return createFlowRequest {
            testRequest()
        }
    }

    private suspend fun testRequest(): WanResponse<List<Tab>> {
        return WanHttpClient.getWanService().test()

    }

    fun testRequestWithFlow2(): Flow<HWeatherResponse> {
        return createFlowRequest {
            testRequest2()
        }
    }

    private suspend fun testRequest2(): HWeatherResponse {
        return TestClient.getApiService(WanService::class.java).test2("116.41,39.92","a65d4d63725e4c2795b6b29748f4755d")

    }
}