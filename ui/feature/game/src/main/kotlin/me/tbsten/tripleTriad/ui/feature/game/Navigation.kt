package me.tbsten.tripleTriad.ui.feature.game

import androidx.navigation.NavGraphBuilder
import kotlinx.serialization.Serializable
import me.tbsten.tripleTriad.domain.game.GamePlayer
import me.tbsten.tripleTriad.ui.feature.game.play.GamePlayScreen
import me.tbsten.tripleTriad.ui.navigation.Navigation
import me.tbsten.tripleTriad.ui.navigation.Screen
import me.tbsten.tripleTriad.ui.navigation.composable
import me.tbsten.tripleTriad.ui.navigation.navigation
import me.tbsten.tripleTriad.ui.navigation.toRoute

@Serializable
data object Game : Navigation

@Serializable
internal data object GamePlay : Screen

@Serializable
internal data class Result(
    val winner: GamePlayer,
) : Screen

fun NavGraphBuilder.game() {
    navigation<Game>(startDestination = GamePlay) {
        composable<GamePlay> {
            GamePlayScreen()
        }

        composable<Result> {
            val route: Result = it.toRoute<Result>()
            TODO("Result Screen: $route")
        }
    }
}
