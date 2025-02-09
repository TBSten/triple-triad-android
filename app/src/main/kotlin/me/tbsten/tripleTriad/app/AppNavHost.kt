package me.tbsten.tripleTriad.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import me.tbsten.tripleTriad.ui.feature.game.Game
import me.tbsten.tripleTriad.ui.feature.game.game
import me.tbsten.tripleTriad.ui.navigation.NavControllerWrapper

@Composable
internal fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navControllerWrapper = NavControllerWrapper(navController)

    NavHost(navController = navController, startDestination = Game, modifier = modifier) {
        game()
    }
}
