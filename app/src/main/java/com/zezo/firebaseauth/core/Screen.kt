package com.zezo.firebaseauth.core

sealed class Screen(val route:String) {
    object SignInScreen:Screen("sign_in")
    object ProfileScreen:Screen("profile")

}