package com.theseuntaylor.picsomeapp.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.theseuntaylor.picsomeapp.core.navigation.Destinations
import com.theseuntaylor.picsomeapp.core.navigation.favouritesRoute
import com.theseuntaylor.picsomeapp.core.navigation.homeRoute

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeRoute, navOptions)
}

fun NavController.navigateToFavourites(navOptions: NavOptions? = null) {
    this.navigate(favouritesRoute, navOptions)
}

@Composable
fun rememberPicSomeAppState(navController: NavController) = remember(navController) {
    PicSomeAppState(navController)
}

@Stable
class PicSomeAppState(val navController: NavController) {

    val topLevelDestinations: List<Destinations> = Destinations.entries

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    private var isBottomBarScrolledAway by mutableStateOf(false)

    val shouldShowBottomBar: Boolean
        @Composable
        get() = !isBottomBarScrolledAway &&
                topLevelDestinations.any { it.destinationRouteName == currentDestination?.route }

    fun onScrollDirectionChanged(isBottomBarVisible: Boolean) {
        isBottomBarScrolledAway = !isBottomBarVisible
    }

    fun navigateToTopDestinations(destination: Destinations) {
        isBottomBarScrolledAway = false
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