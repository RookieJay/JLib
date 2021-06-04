package pers.jay.demo

import kotlinx.coroutines.flow.Flow
import pers.jay.demo.net.WanHttpClient
import pers.jay.demo.net.WanResponse
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
}