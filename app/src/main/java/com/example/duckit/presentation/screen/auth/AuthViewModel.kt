package com.example.duckit.presentation.screen.auth

import com.example.duckit.common.network.ConnectivityObserver
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
) : BaseViewModel<AuthUiState, AuthUiEvent, AuthUiAction>() {

    private val _state = MutableStateFlow(AuthUiState())
    override val state: StateFlow<AuthUiState>
        get() = _state.asStateFlow()

    init {
        safeLaunch {
            connectivityObserver.isConnected.collectLatest { isConnected ->
                if (isConnected) refresh()
                else updateState(ScreenState.Error(message = "Internet unavailable."))
            }
        }
    }

    override fun handleAction(action: AuthUiAction) {
        when (action) {
            is AuthUiAction.OnSignIn -> emitUiEvent(AuthUiEvent.OnSignIn)
            is AuthUiAction.OnSignUp -> emitUiEvent(AuthUiEvent.OnSignUp)
        }
    }

    private suspend fun refresh() {
//        getPostsUseCase().collectLatest { result ->
//            when (result) {
//                is Resource.Error -> updateState(ScreenState.Error(message = result.message.orEmpty()))
//                is Resource.Loading -> updateState(ScreenState.Loading)
//                is Resource.Success -> {
//                    result.data?.let { posts ->
//                        updateState(ScreenState.Data(items = posts.map { it.toUiModel() }))
//                    }
//                }
//            }
//        }
    }

    private fun updateState(screenState: ScreenState) =
        _state.update { it.copy(screenState = screenState) }
}