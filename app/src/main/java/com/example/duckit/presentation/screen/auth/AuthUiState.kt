package com.example.duckit.presentation.screen.auth

import androidx.compose.runtime.Immutable
import com.example.duckit.domain.model.Credentials

@Immutable
data class AuthUiState(
    val screenState: ScreenState = ScreenState.Initial
)

@Immutable
sealed class AuthUiEvent {
    data object OnAuthorized : AuthUiEvent()

    @Immutable
    data class OnError(val message: String) : AuthUiEvent()
}

@Immutable
sealed class AuthUiAction {
    @Immutable
    data class OnSignUp(val credentials: Credentials) : AuthUiAction()

    @Immutable
    data class OnSignIn(val credentials: Credentials) : AuthUiAction()
}

@Immutable
sealed class ScreenState {
    data object Initial : ScreenState()
    data object Loading : ScreenState()

    @Immutable
    data class Error(val message: String) : ScreenState()

    @Immutable
    data class Data(
        val credentials: Credentials = Credentials(),
    ) : ScreenState()
}