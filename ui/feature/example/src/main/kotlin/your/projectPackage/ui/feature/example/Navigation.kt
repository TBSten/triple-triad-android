package your.projectPackage.ui.feature.example

import androidx.navigation.NavGraphBuilder
import kotlinx.serialization.Serializable
import your.projectPackage.ui.feature.example.apiPostList.ExampleApiPostListScreen
import your.projectPackage.ui.feature.example.counter.ExampleCounterScreen
import your.projectPackage.ui.feature.example.localDbUserList.ExampleLocalDbUserListScreen
import your.projectPackage.ui.navigation.NavControllerWrapper
import your.projectPackage.ui.navigation.Navigation
import your.projectPackage.ui.navigation.Screen
import your.projectPackage.ui.navigation.composable
import your.projectPackage.ui.navigation.navigation

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
