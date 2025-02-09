package com.example.duckit.presentation.screen.auth.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.duckit.presentation.navigation.ScreenRoute
import com.example.duckit.presentation.screen.auth.AuthUiAction
import com.example.duckit.presentation.screen.auth.AuthUiEvent
import com.example.duckit.presentation.screen.auth.AuthViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavController,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        lifecycleOwner.lifecycleScope.launch {
            viewModel.events.collectLatest { event ->
                when (event) {
                    AuthUiEvent.OnAuthorized -> navController.navigate(ScreenRoute.Home.name)
                    is AuthUiEvent.OnError -> Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    AccessForm(
        modifier = modifier,
        onSignIn = { creds ->
            viewModel.handleAction(AuthUiAction.OnSignIn(creds))
        },
        onSignUp = { creds ->
            viewModel.handleAction(AuthUiAction.OnSignUp(creds))
        },
    )
}