package me.tbsten.tripleTriad.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import me.tbsten.tripleTriad.error.ApplicationErrorStateHolder
import me.tbsten.tripleTriad.tools.debug.ui.InjectDebugMenu
import me.tbsten.tripleTriad.ui.designSystem.AppTheme
import me.tbsten.tripleTriad.ui.error.HandleErrors

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var applicationErrorStateHolder: ApplicationErrorStateHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                HandleErrors(applicationErrorStateHolder) {
                    InjectDebugMenu()
                    AppNavHost()
                }
            }
        }
    }
}
