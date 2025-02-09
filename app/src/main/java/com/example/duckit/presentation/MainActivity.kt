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
import com.example.duckit.presentation.navigation.DuckitTopAppBar
import com.example.duckit.presentation.navigation.NavRoute
import com.example.duckit.presentation.screen.home.ui.HomeScreen
import com.example.duckit.ui.theme.DuckitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentScreen = navBackStackEntry?.destination?.route
            @Composable fun actions() = if (true) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Sign in",
                    )
                }
            } else {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.Create,
                        contentDescription = "Create post",
                    )
                }
            }

            DuckitTheme {
                Scaffold(
                    topBar = {
                        DuckitTopAppBar(
                            title = currentScreen ?: NavRoute.Home.name,
                            canNavigateBack = currentScreen != NavRoute.Home.name,
                            navigateUp = { navController.navigateUp() },
                            actions = { actions() }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavRoute.Home.name
                    ) {
                        composable(NavRoute.Home.name) {
                            HomeScreen(
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        composable(NavRoute.Access.name) {

                        }
                        composable(NavRoute.Create.name) {

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
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DuckitTheme {
        Greeting("Android")
    }
}

