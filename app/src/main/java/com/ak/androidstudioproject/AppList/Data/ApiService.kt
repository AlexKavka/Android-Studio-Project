package com.ak.androidstudioproject.AppList.Data

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

class PreCardApiService {
    private val client = OkHttpClient()
    private val gson = Gson()

    suspend fun getAppInfo(packageName: String): PreCardDTO? {

        try {
            val url = "https://backapi.rustore.ru/applicationData/overallInfo/$packageName"
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            val jsonString = response.body?.string()

            return jsonString?.let {
                gson.fromJson(it, PreCardDTO::class.java)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}

class ListApiService {
    private val client = OkHttpClient()
    private val gson = Gson()

    suspend fun getAppList(): AppListDTO? {
        try {
            //Тут 3апрос

            val jsonString = """
                {
                    "urls": [
                        "org.telegram.messenger.web", 
                        "com.yandex.browser", 
                        "ru.kinopoisk", 
                        "com.vk.vkvideo", 
                        "ru.rutube.app", 
                        "ru.more.play", 
                        "ru.sberbankmobile", 
                        "com.cyberevo.rustore", 
                        "com.global.loot.rustore"
                    ]
                }
            """.trimIndent()

            return jsonString?.let {
                gson.fromJson(it, AppListDTO::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}