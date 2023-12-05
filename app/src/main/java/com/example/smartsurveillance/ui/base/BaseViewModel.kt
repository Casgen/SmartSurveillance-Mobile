package com.smartsurveillance_mobile.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)

// State
    private val _state = MutableStateFlow<State>(State.None)
    val state: StateFlow<State> = _state.asStateFlow()


    protected fun <Result> launch(
        state: MutableStateFlow<State>? = _state,
        onError: ((Throwable) -> Unit)? = null,
        block: (suspend CoroutineScope.() -> Result),
    ) = scope.launch(throwableHandler(state, onError, block)) {

        // 1. Show loading
        state?.emit(State.Loading)

        // 2. Process operation
        val result = block()

        // 3. Success
        state?.emit(State.Success(result))
    }

    private fun <Result> throwableHandler(
        state: MutableStateFlow<State>?,
        onError: ((Throwable) -> Unit)?,
        block: (suspend CoroutineScope.() -> Result),
    ) = CoroutineExceptionHandler { _, throwable ->
        onError?.invoke(throwable)
        state?.tryEmit(
            State.Failure(
                error = throwable,
                repeat = {
                    repeat(state, onError, block)
                },
            )
        )
    }

    private fun <Result> repeat(
        state: MutableStateFlow<State>?,
        onError: ((Throwable) -> Unit)?,
        block: (suspend CoroutineScope.() -> Result),
    ) {
        launch(state, onError, block)
    }
}