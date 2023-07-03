package com.example.gostore

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gostore.navigations.RouteScreenCartUser
import com.example.gostore.navigations.RouteScreenCategory
import com.example.gostore.navigations.RouteScreenCategoryProduct
import com.example.gostore.navigations.RouteScreenHome
import com.example.gostore.navigations.RouteScreenLogin
import com.example.gostore.navigations.RouteScreenRegister
import com.example.gostore.navigations.Routes
import com.example.gostore.ui.login.Login
import com.example.gostore.ui.theme.GoStoreTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoStoreTheme {

                val navigationController = rememberNavController()

                val activity = LocalContext.current as Activity

                val localConfiguration = LocalConfiguration.current


                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    NavHost(
                        navController = navigationController,
                        startDestination = Routes.ScreenLogin.route
                    ) {

                        composable(Routes.ScreenRegister.route) {

                            RouteScreenRegister(
                                navigationController = navigationController,
                            )

                        }

                        composable(Routes.ScreenLogin.route) {

                            RouteScreenLogin(
                                navigationController = navigationController,
                            )

                        }

                        composable(Routes.ScreenHome.route) {

                            RouteScreenHome(
                                navigationController = navigationController,
                            )

                        }

                        composable(Routes.ScreenCategory.route) {

                            RouteScreenCategory(
                                navigationController = navigationController,
                            )

                        }

                        composable(Routes.ScreenCartUser.route) {

                            RouteScreenCartUser(
                                navigationController = navigationController,
                            )

                        }

                        composable("${Routes.ScreenCategoryProduct.route}",
                            arguments = listOf(
                                navArgument("id") { type = NavType.IntType }
                            )) { backStackEntry ->
                            RouteScreenCategoryProduct(
                                navigationController = navigationController,
                                id = backStackEntry.arguments?.getInt("id") ?: 0,
                            )
                        }

                    }
                }


            }
        }
    }
}
