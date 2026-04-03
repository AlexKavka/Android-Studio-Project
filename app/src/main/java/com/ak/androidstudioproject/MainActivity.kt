package com.ak.androidstudioproject

import android.R.attr.onClick
import android.app.DownloadManager
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ak.androidstudioproject.ui.theme.AndroidStudioProjectTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.Executors


val allApps = setOf("org.telegram.messenger.web", "com.yandex.browser", "ru.kinopoisk", "com.vk.vkvideo", "ru.rutube.app", "ru.more.play", "ru.sberbankmobile", "com.cyberevo.rustore", "com.global.loot.rustore")

@Composable
fun AppPreCard (iconUrl: String?,
                appName: String?,
                shortDescription: String?,
                category: String?,
                onClick: (String?) -> Unit
)
{
    Row (verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp).fillMaxWidth().background(Color.White).clickable { onClick(appName) }) {
        AsyncImage(
            model = "$iconUrl",
            contentDescription = "$appName",
            modifier = Modifier.size(90.dp).clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column (modifier = Modifier.padding(start = 10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text("$appName",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text("$shortDescription",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text("$category",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray
            )
        }
    }
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

@Composable
fun Menu(
    onNavigateToDetail: (String) -> Unit
) {
    Box( modifier = Modifier.fillMaxWidth()
        .background(Color.Blue)){
        Row ( modifier = Modifier.fillMaxWidth().padding(top = 56.dp, start = 8.dp, end = 8.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.rustore_logo),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.height(32.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.someicon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.height(32.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 110.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(5.dp)

        ) {


            for (app in allApps) {
                var appData by remember { mutableStateOf<JSONObject?>(null) }
                var isLoading by remember { mutableStateOf(true) }


                LaunchedEffect(Unit) {
                    val api = ApiService()
                    val result = api.getAppInfo("$app")
                    appData = result
                    isLoading = false
                }

                if (!isLoading) {
                    appData?.let { json ->
                        val body = json.getJSONObject("body")
                        val jsonArray = body.getJSONArray("categories")
                        val categories = (0 until jsonArray.length())
                            .map { jsonArray.getString(it) }
                            .joinToString(" ")
                        AppPreCard(
                            iconUrl = body.optString("iconUrl"),
                            appName = body.optString("appName"),
                            shortDescription = body.optString("shortDescription"),
                            category = categories,
                            onClick = { appName->
                                onNavigateToDetail(appName ?: "")
                            }
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color = Color.LightGray
                        )
                    }
                }
            }

        }

    }

}

@Composable
fun FullCard (
    appName: String?,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Здесь будет полная карточка приложения ${appName ?: "Потеряно"}",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max=300.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onClick) {
            Text("Назад")
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {


                val navController = rememberNavController()


                NavHost(
                    navController = navController,
                    startDestination = "menu"
                ) {

                    composable("menu") {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            Menu(
                                onNavigateToDetail = { appName ->
                                    navController.navigate("detail/$appName")
                                }
                            )
                        }
                    }


                    composable(
                        "detail/{appName}",
                        arguments = listOf(navArgument("appName") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val appName = backStackEntry.arguments?.getString("appName") ?: ""
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            FullCard(appName = appName, onClick = {
                                navController.navigate("menu")
                            })
                        }
                    }
                }

        }
    }
}

