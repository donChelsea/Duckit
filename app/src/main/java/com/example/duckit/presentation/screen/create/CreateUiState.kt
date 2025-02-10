package com.example.duckit.presentation.screen.create

import androidx.compose.runtime.Immutable
import com.example.duckit.domain.model.NewPost

@Immutable
data class CreateUiState(
    val screenState: ScreenState = ScreenState.Initial
)

@Immutable
sealed class CreateUiEvent {
    data object OnPostFinished : CreateUiEvent()

    @Immutable
    data class OnError(val message: String) : CreateUiEvent()
}

@Immutable
sealed class CreateUiAction {
    @Immutable
    data class OnNewPost(val newPost: NewPost) : CreateUiAction()
}

@Immutable
sealed class ScreenState {
    data object Initial : ScreenState()
    @Immutable
    data class Error(val message: String) : ScreenState()
}