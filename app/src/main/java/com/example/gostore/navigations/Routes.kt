package com.example.gostore.navigations

sealed class Routes(val route: String) {

    object ScreenRegister : Routes("ScreenRegister")

    object ScreenLogin : Routes("ScreenLogin")

    object ScreenHome : Routes("ScreenHome")

    object ScreenCategory : Routes("ScreenCategory")

    object ScreenCategoryProduct : Routes("ScreenCategoryProduct/{id}"){
        fun createRoute(id: Int) = "ScreenCategoryProduct/${id}"
    }

    object ScreenCartUser : Routes("ScreenCartUser")


}