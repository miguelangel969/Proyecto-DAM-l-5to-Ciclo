package com.example.gostore.navigations

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gostore.ui.cart.Cart
import com.example.gostore.ui.category.Category
import com.example.gostore.ui.category.CategoryProduct
import com.example.gostore.ui.home.Home
import com.example.gostore.ui.login.Login
import com.example.gostore.ui.login.Register
import java.util.Locale.Category

@Composable
fun RouteScreenLogin(
    navigationController: NavHostController,
) {

    Login(navigationController)

}

@Composable
fun RouteScreenRegister(
    navigationController: NavHostController,
) {

    Register(navigationController)

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RouteScreenHome(
    navigationController: NavHostController,
) {

    Scaffold(bottomBar = {
        HomeBottomAppBar(
            navigationController,
        )
    }) { paddingValues ->

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = paddingValues.calculateBottomPadding())){

        Home()

    }
}

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RouteScreenCategory(
    navigationController: NavHostController,
) {

    Scaffold(bottomBar = {
        HomeBottomAppBar(
            navigationController,
        )
    }) { paddingValues ->

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())){

            Category(navigationController)

        }
    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RouteScreenCategoryProduct(
    id: Int,
    navigationController: NavHostController,
) {

    Scaffold(bottomBar = {
        HomeBottomAppBar(
            navigationController,
        )
    }) { paddingValues ->

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())){

            CategoryProduct(id)

        }
    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RouteScreenCartUser(
    navigationController: NavHostController,
) {

    Scaffold(bottomBar = {
        HomeBottomAppBar(
            navigationController,
        )
    }) { paddingValues ->

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())){

            Cart(navigationController = navigationController)

        }

    }

}

@Composable
fun HomeBottomAppBar(
    navigationController: NavHostController,
) {


    BottomAppBar(
        containerColor = Color.Black,
        contentColor = Color.White,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            IconButton(
                onClick = { navigationController.navigate(Routes.ScreenCartUser.route) }
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Carrito de compras",
                    modifier = Modifier.size(36.dp)
                )
            }

            IconButton(
                onClick = { navigationController.navigate(Routes.ScreenHome.route) }
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(36.dp)
                )
            }

            IconButton(
                onClick = { navigationController.navigate(Routes.ScreenCategory.route) }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Categorías",
                    modifier = Modifier.size(36.dp)
                )
            }

            IconButton(
                onClick = { navigationController.navigate(Routes.ScreenLogin.route) }
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                    modifier = Modifier.size(36.dp)
                )
            }

        }
    }



}
