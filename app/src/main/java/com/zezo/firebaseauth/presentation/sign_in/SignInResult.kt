package com.zezo.firebaseauth.presentation.sign_in

data class SignInResult(
    val data: UserData?,
    val messageError:String?
)

data class UserData(
    val userId:String,
    val userName:String?,
    val photoUrl:String?
)
