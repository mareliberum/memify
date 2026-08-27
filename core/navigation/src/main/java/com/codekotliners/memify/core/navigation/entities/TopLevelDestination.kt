package com.codekotliners.memify.core.navigation.entities

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.codekotliners.memify.core.navigation.R

sealed class TopLevelDestination(
    val labelResId: Int,
    val iconResId: Int,
    val selectedIconResId: Int,
    val route: AppRoute,
) {
    abstract fun isSelected(destination: NavDestination): Boolean

    data object Home :
        TopLevelDestination(
            labelResId = R.string.navigation_home,
            iconResId = R.drawable.outline_home_24,
            selectedIconResId = R.drawable.outline_home_24,
            route = AppRoute.Home,
        ) {
        override fun isSelected(destination: NavDestination): Boolean =
            destination.hasRouteInHierarchy<AppRoute.Home>()
    }

    data object Create :
        TopLevelDestination(
            labelResId = R.string.navigation_create,
            iconResId = R.drawable.add_24dp,
            selectedIconResId = R.drawable.add_24dp,
            route = AppRoute.Create(),
        ) {
        override fun isSelected(destination: NavDestination): Boolean =
            destination.hasRouteInHierarchy<AppRoute.Create>()
    }

    data object Profile :
        TopLevelDestination(
            labelResId = R.string.navigation_profile,
            iconResId = R.drawable.baseline_person_outline_24,
            selectedIconResId = R.drawable.baseline_person_outline_24,
            route = AppRoute.Profile,
        ) {
        override fun isSelected(destination: NavDestination): Boolean =
            destination.hasRouteInHierarchy<AppRoute.Profile>() ||
                destination.hasRouteInHierarchy<AppRoute.Settings>()
    }

    companion object {
        val entries: List<TopLevelDestination> = listOf(Home, Create, Profile)
    }
}

private inline fun <reified T : Any> NavDestination.hasRouteInHierarchy(): Boolean =
    hierarchy.any { destination -> destination.hasRoute<T>() }
