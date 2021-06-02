package pers.jay.demo.net

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonQualifier
import pers.jay.demo.Tab
import retrofit2.http.GET

interface WanService {

    @GET("/project/tree/json")
    suspend fun test(): WanResponse<List<Tab>>
}