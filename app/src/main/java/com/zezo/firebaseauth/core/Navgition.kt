package com.zezo.firebaseauth.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun SetupNav(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.SignInScreen.route
    ){

    }

}