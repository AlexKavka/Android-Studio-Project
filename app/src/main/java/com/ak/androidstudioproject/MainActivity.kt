package com.ak.androidstudioproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ak.androidstudioproject.AppDetailes.Domain.AppDetailsRepository
import com.ak.androidstudioproject.AppDetailes.Presentation.UI.AppDetailsScreen
import com.ak.androidstudioproject.AppList.Domain.AppsListRepository
import com.ak.androidstudioproject.AppList.Presentation.UI.ListOfApps
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

object NavigationConsts {

    private const val LIST_SCREEN = "ListOfApps"
    private const val DETAIL_SCREEN = "detailScreen"

    const val LIST = LIST_SCREEN
    const val DETAIL = "$DETAIL_SCREEN/{appName}"

    fun navToDetail(appName: String): String {
        return "$DETAIL_SCREEN/$appName"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var ListRep: AppsListRepository

    @Inject
    lateinit var FullCardRep: AppDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = NavigationConsts.LIST
                ) {

                    composable(NavigationConsts.LIST) {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            ListOfApps(
                                onNavigateToDetail = { appName ->
                                    navController.navigate(NavigationConsts.navToDetail("$appName"))
                                }
                            )
                        }
                    }

                    composable(
                        NavigationConsts.DETAIL,
                        arguments = listOf(navArgument("appName") {
                            type = NavType.StringType
                        }
                    )
                    ) { backStackEntry ->
                        val appName = backStackEntry.arguments?.getString("appName") ?: ""
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            AppDetailsScreen(
                                modifier = Modifier.padding(innerPadding),
                                packageName = appName,
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