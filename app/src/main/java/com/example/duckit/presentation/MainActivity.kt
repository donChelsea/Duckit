package com.example.duckit.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.duckit.domain.network.TokenManager
import com.example.duckit.presentation.navigation.DuckitTopAppBar
import com.example.duckit.presentation.navigation.ScreenRoute
import com.example.duckit.presentation.screen.auth.ui.AuthScreen
import com.example.duckit.presentation.screen.create.ui.CreateScreen
import com.example.duckit.presentation.screen.home.ui.HomeScreen
import com.example.duckit.ui.theme.DuckitTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentScreen = navBackStackEntry?.destination?.route
            val isUserSignedIn = tokenManager.isTokenSaved

            @Composable fun actions() =
                if (!isUserSignedIn && currentScreen != ScreenRoute.Access.name) {
                    IconButton(onClick = { navController.navigate(ScreenRoute.Access.name) }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Sign in",
                        )
                    }
                } else if (isUserSignedIn && currentScreen != ScreenRoute.Create.name) {
                    IconButton(onClick = { navController.navigate(ScreenRoute.Create.name) }) {
                        Icon(
                            imageVector = Icons.Filled.Create,
                            contentDescription = "Create post",
                        )
                    }
                } else {
                    Unit
                }

            DuckitTheme {
                Scaffold(
                    topBar = {
                        DuckitTopAppBar(
                            title = currentScreen ?: ScreenRoute.Home.name,
                            canNavigateBack = currentScreen != ScreenRoute.Home.name,
                            navigateUp = { navController.navigateUp() },
                            actions = { actions() },
                        )
                    },
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = ScreenRoute.Home.name,
                    ) {
                        composable(ScreenRoute.Home.name) {
                            HomeScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController,
                            )
                        }
                        composable(ScreenRoute.Access.name) {
                            AuthScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController,
                            )
                        }
                        composable(ScreenRoute.Create.name) {
                            CreateScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DuckitTheme {
        Greeting("Android")
    }
}