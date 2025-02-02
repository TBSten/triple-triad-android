package me.tbsten.tripleTriad.ui.feature.example.counter

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.tbsten.tripleTriad.error.ApplicationErrorStateHolder
import me.tbsten.tripleTriad.ui.BaseViewModel

@HiltViewModel
internal class ExampleCounterViewModel @Inject constructor(
    applicationErrorStateHolder: ApplicationErrorStateHolder,
) : BaseViewModel<ExampleCounterUiState, ExampleCounterUiAction>(applicationErrorStateHolder) {
    private val _uiState = MutableStateFlow(
        ExampleCounterUiState(
            count = Random.nextInt(),
        ),
    )
    override val uiState: StateFlow<ExampleCounterUiState> = _uiState.asStateFlow()

    override fun dispatch(action: ExampleCounterUiAction) = when (action) {
        ExampleCounterUiAction.Refresh -> refresh()
    }

    private fun refresh() {
        _uiState.update {
            it.copy(
                count = Random.nextInt(),
            )
        }
    }
}
