package pers.jay.demo.net

import pers.jay.demo.Tab
import retrofit2.http.GET

interface WanService {

    @GET("/project/tree/json")
    suspend fun test(): WanResponse<List<Tab>>
}