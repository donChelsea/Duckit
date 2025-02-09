package com.example.duckit.presentation.screen.home

import com.example.duckit.common.Resource
import com.example.duckit.domain.usecase.GetPostsUseCase
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
    private val getPostsUseCase: GetPostsUseCase
) : BaseViewModel<HomeUiState, HomeUiEvent, HomeUiAction>() {

    private val _state = MutableStateFlow(HomeUiState())
    override val state: StateFlow<HomeUiState>
        get() = _state.asStateFlow()

    init {
        refresh()
    }

    override fun handleAction(action: HomeUiAction) {}

    private fun refresh() {
        safeLaunch {
            getPostsUseCase().collectLatest { result ->
                when (result) {
                    is Resource.Error -> updateState(ScreenState.Error(message = result.message.orEmpty()))
                    is Resource.Loading -> updateState(ScreenState.Loading)
                    is Resource.Success -> {
                        result.data?.let { posts ->
                            updateState(ScreenState.Data(items = posts.map { it.toUiModel() }))
                        }
                    }
                }
            }
        }
    }

    private fun updateState(screenState: ScreenState) =
        _state.update { it.copy(screenState = screenState) }
}