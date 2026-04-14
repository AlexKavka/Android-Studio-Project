package com.ak.androidstudioproject.AppDetailes.Data

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class ApiService {
    private val client = OkHttpClient()
    private val gson = Gson()

    suspend fun getAppInfo(packageName: String): FullCardDTO? {
        try {
            val url = "https://backapi.rustore.ru/applicationData/overallInfo/$packageName"
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            val jsonString = response.body?.string()

            return jsonString?.let {
                gson.fromJson(it, FullCardDTO::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}