package com.example.duckit.presentation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel<UiState, UiEvent, UiAction> : ViewModel() {

    protected val _events = MutableSharedFlow<UiEvent>()

    abstract val state: StateFlow<UiState>

    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    abstract fun handleAction(action: UiAction)

    val safeViewModelScope: CoroutineScope = viewModelScope + CoroutineExceptionHandler { _, t -> Timber.e(t) }

    protected fun emitUiEvent(event: UiEvent) {
        safeLaunch { _events.emit(event) }
    }

    protected fun safeLaunch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        onError: (Throwable) -> Unit = { Timber.e(it) },
        block: suspend CoroutineScope.() -> Unit,
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, t -> onError(t) }
        return viewModelScope.launch(exceptionHandler + context, start, block)
    }

    protected fun <T> Flow<T>.safeLaunchIn(): Job {
        return launchIn(safeViewModelScope)
    }
}
