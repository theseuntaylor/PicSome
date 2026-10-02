package com.theseuntaylor.picsomeapp.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.theseuntaylor.picsomeapp.feature.detail.ui.PhotoDetailsScreen
import com.theseuntaylor.picsomeapp.feature.favourites.ui.ShowFavourites
import com.theseuntaylor.picsomeapp.feature.home.ui.HomeScreen

const val homeRoute = "home_route"
const val detailsRoute = "photo_details/{photoId}"
const val favouritesRoute = "favourites_route"

fun NavGraphBuilder.homeScreen(
    onScrollDirectionChanged: (Boolean) -> Unit,
    onPhotoClicked: (String) -> Unit
) {
    composable(route = homeRoute) {
        HomeScreen(
            onScrollDirectionChanged = onScrollDirectionChanged,
            onPhotoClicked = { photoId -> onPhotoClicked(photoId) })
    }
}

fun NavGraphBuilder.photoDetailsScreen(
    navController: NavController
) {
    composable(route = detailsRoute) {
        PhotoDetailsScreen(
            onUpClick = { navController.popBackStack() })
    }
}

fun NavGraphBuilder.favouritesScreen(onPhotoClicked: (String) -> Unit) {
    composable(route = favouritesRoute) {
        ShowFavourites(onPhotoClicked = { photoId: String -> onPhotoClicked(photoId) })
    }
}