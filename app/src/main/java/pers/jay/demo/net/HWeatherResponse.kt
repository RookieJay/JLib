package pers.jay.demo.net

import com.google.gson.annotations.SerializedName
import pers.jay.library.network.BaseResponse

class HWeatherResponse: BaseResponse<List<Daily>>() {

    @SerializedName("daily")
    override var data: List<Daily>? = null

    override fun isSuccessful() = code == 200
}
