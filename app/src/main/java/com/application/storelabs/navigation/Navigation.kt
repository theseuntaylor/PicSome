package com.application.storelabs.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.storelabs.feature.favourites.ui.ShowFavourites
import com.application.storelabs.feature.home.ui.HomeScreen

const val homeRoute = "home_route"
const val favouritesRoute = "favourites_route"

fun NavGraphBuilder.homeScreen() {
    composable(route = homeRoute) {
        HomeScreen()
    }
}

fun NavGraphBuilder.favouritesScreen() {
    composable(route = favouritesRoute) {
        ShowFavourites()
    }
}