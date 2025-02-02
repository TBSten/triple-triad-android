package your.projectPackage.debug.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import your.projectPackage.error.ApplicationErrorStateHolder
import your.projectPackage.ui.BaseViewModel

@HiltViewModel
internal class DebugMenuViewModel @Inject constructor(exceptionStateHolder: ApplicationErrorStateHolder) : BaseViewModel<DebugMenuUiState, DebugMenuUiAction>(exceptionStateHolder) {
    private val _uiState = MutableStateFlow(DebugMenuUiState(selectedDebugMenuTab = DebugMenuTab.SampleDebugMenuTab))
    override val uiState: StateFlow<DebugMenuUiState> = _uiState.asStateFlow()

    override fun dispatch(action: DebugMenuUiAction) = when (action) {
        is DebugMenuUiAction.SelectTab -> selectTab(action)
    }

    private fun selectTab(action: DebugMenuUiAction.SelectTab) {
        _uiState.update {
            val newSelectedTab = it.debugMenuTabs[action.tabIndex]
            it.copy(
                selectedDebugMenuTab = newSelectedTab,
            )
        }
    }
}

internal data class DebugMenuUiState(
    val selectedDebugMenuTab: DebugMenuTab,
    val debugMenuTabs: List<DebugMenuTab> = DebugMenuTab.entries,
) {
    val selectedTabIndex = debugMenuTabs.indexOf(selectedDebugMenuTab)
}

internal sealed interface DebugMenuUiAction {
    data class SelectTab(val tabIndex: Int) : DebugMenuUiAction
}
