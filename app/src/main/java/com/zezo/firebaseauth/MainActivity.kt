package com.zezo.firebaseauth

import android.app.Application
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.zezo.firebaseauth.core.Screen
import com.zezo.firebaseauth.presentation.sign_in.GoogleAuthUiClint
import com.zezo.firebaseauth.presentation.sign_in.ProfileScreen
import com.zezo.firebaseauth.presentation.sign_in.SignInScreen
import com.zezo.firebaseauth.presentation.sign_in.SingInViewModel
import com.zezo.firebaseauth.ui.theme.FireBaseAuthTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val googleAuthUiClint by lazy {
        GoogleAuthUiClint(
            applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FireBaseAuthTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screen.SignInScreen.route) {

                        composable(Screen.SignInScreen.route) {
                            val viewModel = viewModel<SingInViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            LaunchedEffect(key1 = Unit){
                                if (googleAuthUiClint.getSignInUser()!=null){
                                    navController.navigate(Screen.ProfileScreen.route)
                                }
                            }
                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClint.signInWithIntent(
                                                result.data ?: return@launch

                                            )
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )
                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if (state.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in Successfully",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.navigate(Screen.ProfileScreen.route)
                                    viewModel.resetState()
                                }
                            }
                            SignInScreen(
                                state = state
                            ) {
                                lifecycleScope.launch {
                                    val signInIntentSender = googleAuthUiClint.signIn()
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInIntentSender ?: return@launch
                                        ).build()
                                    )
                                }
                            }
                        }
                        composable(Screen.ProfileScreen.route){
                            ProfileScreen(user = googleAuthUiClint.getSignInUser()) {
                                lifecycleScope.launch {
                                    googleAuthUiClint.signOut()
                                    Toast.makeText(
                                        applicationContext,
                                        "signed out successfully",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.popBackStack()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

