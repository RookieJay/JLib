package pers.jay.demo

import pers.jay.demo.net.WanHttpClient
import pers.jay.demo.net.WanService
import pers.jay.library.base.repository.BaseRepository

open class WanRepo: BaseRepository() {

    protected val wanService: WanService by lazy {
        WanHttpClient.getWanService()
    }

}