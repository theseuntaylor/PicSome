package com.theseuntaylor.picsomeapp.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.theseuntaylor.picsomeapp.navigation.Destinations
import com.theseuntaylor.picsomeapp.navigation.favouritesRoute
import com.theseuntaylor.picsomeapp.navigation.homeRoute

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeRoute, navOptions)
}

fun NavController.navigateToFavourites(navOptions: NavOptions? = null) {
    this.navigate(favouritesRoute, navOptions)
}

@Stable
class PicSomeAppState(private val navController: NavController) {

    val topLevelDestinations: List<Destinations> = Destinations.values().asList()

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    fun navigateToTopDestinations(destination: Destinations) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
        when (destination) {
            Destinations.HOME -> navController.navigateToHome(topLevelNavOptions)
            Destinations.FAVOURITES -> navController.navigateToFavourites(topLevelNavOptions)
        }
    }
}