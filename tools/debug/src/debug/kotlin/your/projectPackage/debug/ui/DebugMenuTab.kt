package your.projectPackage.debug.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Stable
internal enum class DebugMenuTab {
    SampleDebugMenuTab {
        override val tabText = "sample debug menu tab"

        @Composable
        override fun Content() {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("TODO Add your debug menus")
            }
        }
    },
    ;

    abstract val tabText: String

    @Composable
    abstract fun Content()
}
