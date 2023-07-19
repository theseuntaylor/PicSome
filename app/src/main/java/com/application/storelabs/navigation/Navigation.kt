package com.application.storelabs.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.storelabs.Greeting


const val homeRoute = "home_route"
const val favouritesRoute = "favourites_route"

fun NavGraphBuilder.homeScreen() {
    composable(route = homeRoute) {
        Greeting(name = "Solomon")
    }
}

fun NavGraphBuilder.favouritesScreen() {
    composable(route = favouritesRoute) {
        Greeting(name = "George-Taylor")
    }
}