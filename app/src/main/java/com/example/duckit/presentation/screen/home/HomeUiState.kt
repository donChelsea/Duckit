package com.example.duckit.presentation.screen.home

import androidx.compose.runtime.Immutable
import com.example.duckit.presentation.model.PostUiModel

@Immutable
data class HomeUiState(
    val screenState: ScreenState = ScreenState.Initial
)

@Immutable
sealed class HomeUiEvent

@Immutable
sealed class HomeUiAction

@Immutable
sealed class ScreenState {
    data object Initial : ScreenState()
    data object Loading : ScreenState()
    @Immutable
    data class Error(val message: String) : ScreenState()
    @Immutable
    data class Data(
        val items: List<PostUiModel> = emptyList(),
        val isSignedIn: Boolean = false,
    ) : ScreenState()
}