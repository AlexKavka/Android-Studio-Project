package com.ak.androidstudioproject.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

data class AppUrls (
    val urls : List<String>
)

data class PreCardInfo (
    val url : String,
    val iconUrl : String,
    val appName : String,
    val shortDescription : String,
    val categories: List<String>
)

data class FullCardInfo (
    val url : String,
    val iconUrl : String,
    val appName : String,
    val shortDescription : String,
    val categories : List<String>,
    val screenshots : List<String>,
    val ageRating : String,
    val developer : String,
    val appSize: Long
)

interface AppsRepository {
    suspend fun getAppUrls(): AppUrls?
    suspend fun getAppPreCard(packageName: String) : PreCardInfo?
    suspend fun getFullAppInfo(packageName: String) : FullCardInfo?
    fun clearCache()
}

class ApiService {
    private val client = OkHttpClient()

    suspend fun getAppInfo(packageName: String): JSONObject? = withContext(Dispatchers.IO) {
        try {
            val url = "https://backapi.rustore.ru/applicationData/overallInfo/$packageName"
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val jsonString = response.body?.string()
            return@withContext if (jsonString != null) JSONObject(jsonString) else null
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }
}

class AppsRepositoryImpl(
    private val apiService: ApiService
) : AppsRepository {

    private val preCardCache =  mutableMapOf<String, PreCardInfo>()
    private val fullCardCache =   mutableMapOf<String, FullCardInfo>()

    override suspend fun getAppUrls(): AppUrls? {
        // Пока заглушка:
        return AppUrls(
            urls = listOf("org.telegram.messenger.web", "com.yandex.browser", "ru.kinopoisk", "com.vk.vkvideo", "ru.rutube.app", "ru.more.play", "ru.sberbankmobile", "com.cyberevo.rustore", "com.global.loot.rustore")
        )
    }

    override suspend fun getAppPreCard(packageName: String): PreCardInfo? {

        preCardCache[packageName]?.let {
            return it
        }

        val json = apiService.getAppInfo(packageName)

        return json?.let {
            val body = it.getJSONObject("body")
            val jsonArray = body.getJSONArray("categories")
            val screens = body.getJSONArray("fileUrls")

            preCardCache[packageName] = PreCardInfo(
                url = packageName,
                appName = body.optString("appName"),
                shortDescription = body.optString("shortDescription"),
                iconUrl = body.optString("iconUrl"),
                categories = (0 until jsonArray.length()).map { jsonArray.getString(it) }
            )

            PreCardInfo(
                url = packageName,
                appName = body.optString("appName"),
                shortDescription = body.optString("shortDescription"),
                iconUrl = body.optString("iconUrl"),
                categories = (0 until jsonArray.length()).map { jsonArray.getString(it) }
            )
        }
    }

    override suspend fun getFullAppInfo(packageName: String): FullCardInfo? {

        fullCardCache[packageName]?.let {
            return it
        }

        val json = apiService.getAppInfo(packageName)

        return json?.let {
            val body = it.getJSONObject("body")
            val jsonArray = body.getJSONArray("categories")
            val screens = body.getJSONArray("fileUrls")
            val ageRestr = body.getJSONObject("ageRestriction")

            fullCardCache[packageName] = FullCardInfo(
                url = packageName,
                appName = body.optString("appName"),
                shortDescription = body.optString("shortDescription"),
                iconUrl = body.optString("iconUrl"),
                categories = (0 until jsonArray.length()).map { jsonArray.getString(it) },
                screenshots = (0 until screens.length()).map { screens.getJSONObject(it).getString("fileUrl") },
                ageRating = ageRestr.optString("category"),
                developer = body.optString("companyName"),
                appSize = body.optLong("fileSize")
            )

            FullCardInfo(
                url = packageName,
                appName = body.optString("appName"),
                shortDescription = body.optString("shortDescription"),
                iconUrl = body.optString("iconUrl"),
                categories = (0 until jsonArray.length()).map { jsonArray.getString(it) },
                screenshots = (0 until screens.length()).map { screens.getJSONObject(it).getString("fileUrl") },
                ageRating = ageRestr.optString("category"),
                developer = body.optString("companyName"),
                appSize = body.optLong("fileSize")
            )
        }
    }

    override fun clearCache() {
        preCardCache.clear()
    }
}
