package com.ak.androidstudioproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ak.androidstudioproject.AppDetailes.Presentation.UI.AppDetailsScreen
import com.ak.androidstudioproject.AppList.Presentation.UI.ListOfApps
import com.ak.androidstudioproject.model.ApiService
import com.ak.androidstudioproject.model.AppsRepositoryImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rep = AppsRepositoryImpl(ApiService())

        setContent {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "ListOfApps"
                ) {

                    composable("ListOfApps") {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            ListOfApps(
                                rep,
                                onNavigateToDetail = { appName ->
                                    navController.navigate("detailScreen/$appName")
                                }
                            )
                        }
                    }

                    composable(
                        "detailScreen/{appName}",
                        arguments = listOf(navArgument("appName") {
                            type = NavType.StringType
                        }
                    )
                    ) { backStackEntry ->
                        val appName = backStackEntry.arguments?.getString("appName") ?: ""
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            AppDetailsScreen(
                                packageName = appName,
                                rep,
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
        }
    }
}