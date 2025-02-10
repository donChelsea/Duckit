package com.example.duckit.presentation.screen.create

import com.example.duckit.common.network.ConnectivityObserver
import com.example.duckit.common.network.Resource
import com.example.duckit.domain.model.Credentials
import com.example.duckit.presentation.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import java.io.File
import java.io.InputStream
import javax.inject.Inject


@HiltViewModel
class CreateViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver,
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
                val uri = action.newPost.image
                val s = convertURIToURL(uri.toString())
                println("create: ${s.}")
                println("create: https://${uri?.host}${uri?.encodedPath}\"")
            }
        }
    }


    fun convertURIToURL(uriString: String): InputStream? {
        var file = File("filename")

        val uri = file.toURI()
        file = File(uri.toURL().file)
        val `is` = uri.toURL().openStream()
        `is`.close()
        return `is`
    }

    private fun processToken(credentials: Credentials, result: Resource<String>) {
        when (result) {
            is Resource.Error -> emitUiEvent(CreateUiEvent.OnError(message = result.message.orEmpty()))
            is Resource.Success -> {}

            else -> {}
        }
    }

    private fun updateState(screenState: ScreenState) =
        _state.update { it.copy(screenState = screenState) }
}