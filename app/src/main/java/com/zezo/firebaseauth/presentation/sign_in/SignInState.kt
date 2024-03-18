package com.zezo.firebaseauth.presentation.sign_in

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signError: String? = null
)
