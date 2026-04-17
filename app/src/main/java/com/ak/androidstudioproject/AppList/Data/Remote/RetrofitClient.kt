package com.ak.androidstudioproject.AppList.Data.Remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://www.rustore.ru/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofitJson = Retrofit.Builder()
        .baseUrl("https://backapi.rustore.ru/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitHtml = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    val preCardApiService: PreCardRetrofitApiService =
        retrofitJson.create(PreCardRetrofitApiService::class.java)

    val listApiService: ListRetrofitApiService =
        retrofitHtml.create(ListRetrofitApiService::class.java)
}