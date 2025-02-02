package me.tbsten.tripleTriad.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.PopUpToBuilder

class NavControllerWrapper(private val navController: NavController) {
    fun navigate(
        route: Route,
        builder: NavOptionsBuilderWrapper.() -> Unit = {},
    ) {
        navController.navigate(route) {
            NavOptionsBuilderWrapper(this).builder()
        }
    }

    fun popBackStack() {
        navController.popBackStack()
    }
}

class NavOptionsBuilderWrapper(
    @PublishedApi
    internal val navOptionsBuilder: NavOptionsBuilder,
) {
    var launchSingleTop: Boolean by navOptionsBuilder::launchSingleTop
    var restoreState: Boolean by navOptionsBuilder::restoreState

    inline fun <reified R : Route> popUpTo(
        noinline popUpToBuilder: PopUpToBuilder.() -> Unit = {},
    ) {
        navOptionsBuilder.popUpTo<R>(popUpToBuilder)
    }
}
