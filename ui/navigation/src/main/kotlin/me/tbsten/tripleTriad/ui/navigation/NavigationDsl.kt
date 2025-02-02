@file:Suppress("ForbiddenImport")

package me.tbsten.tripleTriad.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import kotlin.reflect.KType

@PublishedApi
internal val customTypeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap()

inline fun <reified S : Screen> NavGraphBuilder.composable(
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline enterTransition: (
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?
    )? = null,
    noinline exitTransition: (
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?
    )? = null,
    noinline popEnterTransition: (
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?
    )? = enterTransition,
    noinline popExitTransition: (
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?
    )? = exitTransition,
    noinline sizeTransform: (
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?
    )? = null,
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) = composable<S>(
    typeMap = customTypeMap,
    deepLinks = deepLinks,
    enterTransition = enterTransition,
    exitTransition = exitTransition,
    popEnterTransition = popEnterTransition,
    popExitTransition = popExitTransition,
    sizeTransform = sizeTransform,
    content = content,
)

inline fun <reified N : Navigation> NavGraphBuilder.navigation(
    startDestination: Route,
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline enterTransition: (
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?
    )? = null,
    noinline exitTransition: (
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?
    )? = null,
    noinline popEnterTransition: (
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?
    )? = enterTransition,
    noinline popExitTransition: (
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?
    )? = exitTransition,
    noinline sizeTransform: (
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?
    )? = null,
    noinline builder: NavGraphBuilder.() -> Unit,
) = navigation<N>(
    startDestination = startDestination,
    typeMap = customTypeMap,
    deepLinks = deepLinks,
    enterTransition = enterTransition,
    exitTransition = exitTransition,
    popEnterTransition = popEnterTransition,
    popExitTransition = popExitTransition,
    sizeTransform = sizeTransform,
    builder = builder,
)

inline fun <reified D : Dialog> NavGraphBuilder.dialog(
    deepLinks: List<NavDeepLink> = emptyList(),
    dialogProperties: DialogProperties = DialogProperties(),
    noinline content: @Composable (NavBackStackEntry) -> Unit,
) = dialog<D>(
    typeMap = customTypeMap,
    deepLinks = deepLinks,
    dialogProperties = dialogProperties,
    content = content,
)

inline fun <reified D : Dialog> SavedStateHandle.toRoute() = toRoute<D>(
    typeMap = customTypeMap,
)
