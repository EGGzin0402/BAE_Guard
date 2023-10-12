package com.example.baeguard

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.baeguard.presenter.singin.GoogleAuthUiClient
import com.example.baeguard.presenter.view.tela_cadastro
import com.example.baeguard.presenter.view.tela_login
import com.example.baeguard.presenter.viewmodel.SignInViewModel
import com.example.baeguard.ui.theme.BaeguardTheme
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaeguardTheme {

                window.statusBarColor=getColor(R.color.black)
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "tela_login"){
                    composable(
                        route = "tela_login"
                    ){

                        val signInViewModel by viewModels<SignInViewModel>()
                        val state by signInViewModel.state.collectAsStateWithLifecycle()

                        LaunchedEffect(key1 = Unit){
                            if (googleAuthUiClient.getSignedInUser() != null){
                                var nav = Intent(this@MainActivity, MainScreenActivity::class.java)
                                startActivity(nav)
                            }
                        }

                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult(),
                            onResult = { result ->
                                if (result.resultCode == RESULT_OK) {
                                    lifecycleScope.launch {
                                        val signInResult = googleAuthUiClient.signInWithIntent(
                                            intent = result.data ?: return@launch
                                        )
                                        signInViewModel.onSignInResult(signInResult)

                                        // Redirecione para MainScreenActivity após o login bem-sucedido
                                        var nav = Intent(this@MainActivity, MainScreenActivity::class.java)
                                        startActivity(nav)
                                    }
                                }
                            }
                        )

                        tela_login(
                            state = state,
                            onSignInClick = {
                                lifecycleScope.launch {
                                    val signInIntentSender = googleAuthUiClient.signIn()
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInIntentSender ?: return@launch
                                        ).build()
                                    )
                                }
                            },
                            navController = navController,
                            mainScreen = {
                                var nav = Intent(this@MainActivity, MainScreenActivity::class.java)
                                startActivity(nav)
                            }
                        )
                    }

                    composable(
                        route = "tela_cadastro"
                    ){
                        tela_cadastro(navController)
                    }

                }

            }
        }
    }

}



