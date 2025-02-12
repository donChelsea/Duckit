package com.example.duckit.presentation.screen.home

import com.example.duckit.domain.network.ConnectivityObserver
import com.example.duckit.domain.network.Resource
import com.example.duckit.domain.network.TokenManager
import com.example.duckit.domain.usecase.DownvoteUseCase
import com.example.duckit.domain.usecase.GetPostsUseCase
import com.example.duckit.domain.usecase.UpvoteUseCase
import com.example.duckit.presentation.model.mapper.toUiModel
import com.example.duckit.presentation.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val upvoteUseCase: UpvoteUseCase,
    private val downvoteUseCase: DownvoteUseCase,
    private val connectivityObserver: ConnectivityObserver,
    private val tokenManager: TokenManager,
) : BaseViewModel<HomeUiState, HomeUiEvent, HomeUiAction>() {

    private val _state = MutableStateFlow(HomeUiState())
    override val state: StateFlow<HomeUiState>
        get() = _state.asStateFlow()

    init {
        safeLaunch {
            connectivityObserver.isConnected.collectLatest { isConnected ->
                if (isConnected) refresh()
                else updateState(ScreenState.Error(message = "Internet unavailable."))
            }
        }
    }

    override fun handleAction(action: HomeUiAction) {
        when (action) {
            is HomeUiAction.OnDownvote -> safeLaunch {
                processVote(downvoteUseCase(action.postId))
            }

            is HomeUiAction.OnUpvote -> safeLaunch {
                processVote(upvoteUseCase(action.postId))
            }

            HomeUiAction.OnConfirmDialog -> emitUiEvent(HomeUiEvent.OnConfirmDialog)
        }
    }

    private suspend fun refresh() {
        getPostsUseCase().collectLatest { result ->
            when (result) {
                is Resource.Error -> updateState(ScreenState.Error(message = result.message.orEmpty()))
                is Resource.Loading -> updateState(ScreenState.Loading)
                is Resource.Success -> result.data?.let { posts ->
                    updateState(
                        ScreenState.Data(
                            items = posts.map { it.toUiModel() },
                            isSignedIn = tokenManager.isTokenSaved,
                        )
                    )
                }
            }
        }
    }

    private fun processVote(result: Resource<Int>) {
        when (result) {
            is Resource.Error -> emitUiEvent(
                HomeUiEvent.OnError(
                    result.message ?: "Unknown error."
                )
            )

            else -> {}
        }
    }

    private fun updateState(screenState: ScreenState) =
        _state.update { it.copy(screenState = screenState) }
}