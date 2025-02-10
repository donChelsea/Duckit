package com.example.duckit.presentation.screen.create

import com.example.duckit.common.network.ConnectivityObserver
import com.example.duckit.common.network.Resource
import com.example.duckit.domain.usecase.CreatePostUseCase
import com.example.duckit.presentation.screen.auth.AuthUiEvent
import com.example.duckit.presentation.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class CreateViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver,
    private val createPostUseCase: CreatePostUseCase,
) : BaseViewModel<CreateUiState, CreateUiEvent, CreateUiAction>() {

    private val _state = MutableStateFlow(CreateUiState())
    override val state: StateFlow<CreateUiState>
        get() = _state.asStateFlow()

    init {
        safeLaunch {
            connectivityObserver.isConnected.collectLatest { isConnected ->
                if (!isConnected) updateState(ScreenState.Error(message = "Internet unavailable."))
            }
        }
    }

    override fun handleAction(action: CreateUiAction) {
        when (action) {
            is CreateUiAction.OnNewPost -> {
                safeLaunch {
                    val result = createPostUseCase(action.newPost)
                    when (result) {
                        is Resource.Error -> emitUiEvent(CreateUiEvent.OnError(message = result.message.orEmpty()))
                        is Resource.Success -> {
                            emitUiEvent(CreateUiEvent.OnPostFinished)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun updateState(screenState: ScreenState) =
        _state.update { it.copy(screenState = screenState) }
}