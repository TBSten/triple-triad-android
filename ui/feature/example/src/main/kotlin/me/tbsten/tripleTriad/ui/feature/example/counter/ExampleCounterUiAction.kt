package me.tbsten.tripleTriad.ui.feature.example.counter

internal sealed interface ExampleCounterUiAction {
    data object Refresh : ExampleCounterUiAction
}
