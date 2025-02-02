package your.projectPackage.ui.feature.example.counter

internal sealed interface ExampleCounterUiAction {
    data object Refresh : ExampleCounterUiAction
}
