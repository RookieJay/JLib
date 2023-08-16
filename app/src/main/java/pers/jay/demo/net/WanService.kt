package pers.jay.demo.net

import pers.jay.demo.data.ArticleInfo
import pers.jay.demo.data.Tab
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WanService {

    @GET("/project/tree/json")
    suspend fun test(): WanResponse<List<Tab>>

    @GET("/article/list/{page}/json")
    suspend fun homeArticles(@Path("page") page: Int): WanResponse<ArticleInfo>

    @GET("/v7/weather/7d/")
    suspend fun test2(
        @Query("location") location: String, @Query("key") key: String
    ): HWeatherResponse
}