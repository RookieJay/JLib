package pers.jay.demo.databinding

import kotlinx.coroutines.flow.Flow
import pers.jay.demo.WanRepo
import pers.jay.demo.data.Tab
import pers.jay.demo.net.WanResponse

class InfoModel: WanRepo() {
    fun loadData(): Flow<WanResponse<List<Tab>>> {
        return createFlowRequest {
            wanService.test()
        }
    }

}
