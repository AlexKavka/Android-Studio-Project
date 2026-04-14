package com.ak.androidstudioproject.AppList.Data.Remote

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ListRetrofitApiService {

    @GET("catalog/search")
    suspend fun getAppList(
        @Query("query") query: String = "a",
        @Header("User-Agent") userAgent: String = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
    ): String
}

interface PreCardRetrofitApiService {

    @GET("applicationData/overallInfo/{packageName}")
    suspend fun getAppInfo(
        @Path("packageName") packageName: String
    ): PreCardDTO
}