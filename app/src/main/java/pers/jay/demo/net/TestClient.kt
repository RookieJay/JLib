package pers.jay.demo.net

import pers.jay.library.network.NetworkManager

object TestClient: NetworkManager(){
    override fun getBaseUrl(): String {
        return "https://devapi.qweather.com"
    }
}