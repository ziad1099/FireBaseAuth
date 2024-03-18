package com.zezo.firebaseauth.presentation.sign_in

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SingInViewModel:ViewModel() {
    private val _state= MutableStateFlow(SignInState())
    val state=_state.asStateFlow()

    fun onSignInResult(result: SignInResult){
        _state.update {it.copy(
            isSignInSuccessful = result.data != null,
            signError = result.messageError
        )
        }
    }

    fun resetState(){
        _state.update { SignInState() }
    }
}