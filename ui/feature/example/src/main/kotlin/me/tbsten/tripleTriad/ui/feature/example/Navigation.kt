package me.tbsten.tripleTriad.ui.feature.example

import androidx.navigation.NavGraphBuilder
import kotlinx.serialization.Serializable
import me.tbsten.tripleTriad.ui.feature.example.apiPostList.ExampleApiPostListScreen
import me.tbsten.tripleTriad.ui.feature.example.counter.ExampleCounterScreen
import me.tbsten.tripleTriad.ui.feature.example.localDbUserList.ExampleLocalDbUserListScreen
import me.tbsten.tripleTriad.ui.navigation.NavControllerWrapper
import me.tbsten.tripleTriad.ui.navigation.Navigation
import me.tbsten.tripleTriad.ui.navigation.Screen
import me.tbsten.tripleTriad.ui.navigation.composable
import me.tbsten.tripleTriad.ui.navigation.navigation

@Serializable
data object Examples : Navigation

@Serializable
data object ExampleCounter : Screen

@Serializable
data object ExampleLocalDbUserList : Screen

@Serializable
data object ExampleApiPostList : Screen

fun NavGraphBuilder.examples(
    navControllerWrapper: NavControllerWrapper,
) {
    navigation<Examples>(startDestination = ExampleCounter) {
        composable<ExampleCounter> {
            ExampleCounterScreen(
                navigateToUserList = { navControllerWrapper.navigate(ExampleLocalDbUserList) },
                navigateToPostList = { navControllerWrapper.navigate(ExampleApiPostList) },
            )
        }
        composable<ExampleLocalDbUserList> {
            ExampleLocalDbUserListScreen()
        }

        composable<ExampleApiPostList> {
            ExampleApiPostListScreen()
        }
    }
}
