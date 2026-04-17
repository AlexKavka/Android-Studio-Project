package com.ak.androidstudioproject.AppDetailes.Data.Remote

import com.ak.androidstudioproject.AppDetailes.Data.Remote.*
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitApiService {

    @GET("applicationData/overallInfo/{packageName}")
    suspend fun getAppInfo(
        @Path("packageName") packageName: String
    ): FullCardDTO
}