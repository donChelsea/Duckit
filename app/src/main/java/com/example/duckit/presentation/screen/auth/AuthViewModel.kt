package com.example.duckit.presentation.screen.auth

import com.example.duckit.common.auth.UserManager
import com.example.duckit.common.network.ConnectivityObserver
import com.example.duckit.common.network.Resource
import com.example.duckit.domain.model.Credentials
import com.example.duckit.domain.usecase.SignInUseCase
import com.example.duckit.domain.usecase.SignUpUseCase
import com.example.duckit.presentation.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver,
    private val userManager: UserManager,
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
) : BaseViewModel<AuthUiState, AuthUiEvent, AuthUiAction>() {

    private val _state = MutableStateFlow(AuthUiState())
    override val state: StateFlow<AuthUiState>
        get() = _state.asStateFlow()

    init {
        safeLaunch {
            connectivityObserver.isConnected.collectLatest { isConnected ->
                if (!isConnected) updateState(ScreenState.Error(message = "Internet unavailable."))
            }
        }
    }

    override fun handleAction(action: AuthUiAction) {
        when (action) {
            is AuthUiAction.OnSignIn -> safeLaunch {
                processToken(
                    credentials = action.credentials,
                    result = signInUseCase(action.credentials)
                )
            }

            is AuthUiAction.OnSignUp -> safeLaunch {
                processToken(
                    credentials = action.credentials,
                    result = signUpUseCase(action.credentials)
                )
            }
        }
    }

    private fun processToken(credentials: Credentials, result: Resource<String>) {
        when (result) {
            is Resource.Error -> emitUiEvent(AuthUiEvent.OnError(message = result.message.orEmpty()))
            is Resource.Success -> {
                val token = result.data.orEmpty()
                userManager.signIn(
                    credentials = credentials,
                    token = token,
                )
                emitUiEvent(AuthUiEvent.OnAuthorized)
            }

            else -> {}
        }
    }

    private fun updateState(screenState: ScreenState) =
        _state.update { it.copy(screenState = screenState) }
}