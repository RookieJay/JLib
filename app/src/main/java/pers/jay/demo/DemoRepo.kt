package pers.jay.demo

import kotlinx.coroutines.flow.Flow
import pers.jay.demo.net.TestHttpClient
import pers.jay.demo.net.WanResponse
import pers.jay.library.base.repository.BaseRepository

class DemoRepo : BaseRepository() {

    fun testRequestWithFlow(): Flow<WanResponse<List<Tab>>> {
        return requestOnFlow {
            testRequest()
        }
    }

    private suspend fun testRequest(): WanResponse<List<Tab>> {
        return TestHttpClient.getWanService().test()

    }
}