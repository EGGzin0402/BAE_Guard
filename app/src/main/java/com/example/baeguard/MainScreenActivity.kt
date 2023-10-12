package com.example.baeguard

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.baeguard.presenter.singin.GoogleAuthUiClient
import com.example.baeguard.presenter.view.Add_DispoScreen
import com.example.baeguard.presenter.view.Add_DispoScreen_Qr
import com.example.baeguard.presenter.view.Disp_DtScreen
import com.example.baeguard.presenter.view.EdEmailScreen
import com.example.baeguard.presenter.view.EdNomeScreen
import com.example.baeguard.presenter.view.EdSenhaScreen
import com.example.baeguard.presenter.view.HistoricoScreen
import com.example.baeguard.presenter.view.HomeScreen
import com.example.baeguard.presenter.view.PerfilScreen
import com.example.baeguard.presenter.viewmodel.DispDtViewModel
import com.example.baeguard.ui.theme.BaeguardTheme
import com.example.baeguard.viewmodel.HomeViewModel
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainScreenActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private val homeViewModel by viewModels<HomeViewModel>()
    private val dispDtViewModel by viewModels<DispDtViewModel>()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val currentRoute = currentRoute(navController)
            val topBarTitle = remember { mutableStateOf(getTopBarTitle(currentRoute)) }
            val showButton = currentRoute != NavigationItem.Home.route
            BaeguardTheme {
                androidx.compose.material.Surface(color = colorResource(id = R.color.colorPrimaryDark)) {
                    Scaffold(
                        topBar = {
                            TopBar(
                                title = topBarTitle.value,
                                showButton = showButton,
                                onBackPressed = navController::navigateUp
                            )
                        },
                        bottomBar = { BottomNavigationBar(navController) },
                        content = { padding ->
                            Box(modifier = Modifier.padding(padding)) {

                                val onRouteChanged = { route: String ->
                                    topBarTitle.value = getTopBarTitle(route)
                                }
                                NavHost(navController, startDestination = NavigationItem.Home.route) {
                                    composable(NavigationItem.Home.route) {
                                        onRouteChanged(NavigationItem.Home.route)
                                        HomeScreen(navController, homeViewModel)
                                    }
                                    composable(NavigationItem.Perfil.route) {
                                        onRouteChanged(NavigationItem.Perfil.route)
                                        PerfilScreen(
                                            userData = googleAuthUiClient.getSignedInUser(),
                                            navController = navController,
                                            onSignOut = {
                                                lifecycleScope.launch {
                                                    googleAuthUiClient.signOut()

                                                    Toast.makeText(
                                                        applicationContext,
                                                        "Signed Out",
                                                        Toast.LENGTH_LONG
                                                    ).show()

                                                    var nav = Intent(this@MainScreenActivity, MainActivity::class.java)
                                                    startActivity(nav)

                                                }

                                            }
                                        )
                                    }
                                    composable(NavigationItem.Historico.route) {
                                        onRouteChanged(NavigationItem.Historico.route)
                                        HistoricoScreen()
                                    }

                                    composable(NavigationItem.Ed_senha.route) {
                                        onRouteChanged(NavigationItem.Ed_senha.route)
                                        EdSenhaScreen(navController::navigateUp)
                                    }
                                    composable(NavigationItem.Ed_email.route) {
                                        onRouteChanged(NavigationItem.Ed_email.route)
                                        EdEmailScreen(navController::navigateUp)
                                    }
                                    composable(NavigationItem.Ed_Nome.route) {
                                        onRouteChanged(NavigationItem.Ed_Nome.route)
                                        EdNomeScreen(navController::navigateUp)
                                    }
                                    composable(NavigationItem.Add_dispositivo.route) {
                                        onRouteChanged(NavigationItem.Add_dispositivo.route)
                                        Add_DispoScreen(navController)
                                    }
                                    composable(NavigationItem.Add_Qr.route) {
                                        onRouteChanged(NavigationItem.Add_Qr.route)
                                        Add_DispoScreen_Qr(navController::navigateUp)
                                    }

                                    composable(NavigationItem.Disp_Dt("{deviceId}").route) {
                                        val deviceId = it.arguments?.getString("deviceId")
                                        onRouteChanged(NavigationItem.Disp_Dt(deviceId!!).route)
                                        Disp_DtScreen(deviceId!!, dispDtViewModel, navController)
                                    }
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    return navBackStackEntry?.destination?.route
}

fun getTopBarTitle(route: String?): String {
    return when (route) {
        NavigationItem.Home.route -> "Home"
        NavigationItem.Perfil.route -> "Perfil"
        NavigationItem.Historico.route -> "Histórico"
        NavigationItem.Ed_senha.route -> "Editar Senha"
        NavigationItem.Ed_email.route -> "Editar Email"
        NavigationItem.Ed_Nome.route -> "Editar Nome"
        NavigationItem.Add_dispositivo.route -> "Adicionar Dispositivo"
        NavigationItem.Add_Qr.route -> "Escanear QR Code"
        "disp_dt" -> "Detalhes do Dispositivo"
        else -> "B. A. E. Guard"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, showButton: Boolean = true, onBackPressed: () -> Unit = {}) {
    androidx.compose.material.Surface(color = colorResource(id = R.color.colorPrimaryDark)) {
        TopAppBar(
            colors = TopAppBarDefaults.smallTopAppBarColors(colorResource(id = R.color.colorPrimary)),
            title = {
                androidx.compose.material.Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            navigationIcon = {
                if (showButton) {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                }
            }
        )
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Perfil,
        NavigationItem.Historico,
    )
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.colorPrimary),
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { androidx.compose.material.Text(text = item.title) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }

}
