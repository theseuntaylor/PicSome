package com.theseuntaylor.picsomeapp.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.theseuntaylor.picsomeapp.feature.favourites.ui.ShowFavourites
import com.theseuntaylor.picsomeapp.feature.home.ui.HomeScreen

const val homeRoute = "home_route"
const val favouritesRoute = "favourites_route"

fun NavGraphBuilder.homeScreen(snackBarHostState: SnackbarHostState) {
    composable(route = homeRoute) {
        HomeScreen(snackBarHostState = snackBarHostState)
    }
}

fun NavGraphBuilder.favouritesScreen() {
    composable(route = favouritesRoute) {
        ShowFavourites()
    }
}