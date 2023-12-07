package com.theseuntaylor.picsomeapp.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {

    data object Home : Screen("home")

    data object FullImage : Screen(
        route = "fullScreen/{pictureId}",
        navArguments = listOf(navArgument("pictureId") {
            type = NavType.StringType
        })
    ) {
        fun createRoute(pictureId: String) = "fullScreen/${pictureId}"
    }

    data object Favourites : Screen(
        route = "favourites",
    )
}