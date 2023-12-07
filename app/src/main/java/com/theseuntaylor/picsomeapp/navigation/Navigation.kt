package com.theseuntaylor.picsomeapp.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.theseuntaylor.picsomeapp.feature.favourites.ui.ShowFavourites
import com.theseuntaylor.picsomeapp.feature.home.ui.HomeScreen
import com.theseuntaylor.picsomeapp.feature.view_full_image.ui.FullImageScreen

fun NavGraphBuilder.homeScreen(snackBarHostState: SnackbarHostState, navController: NavController) {
    composable(
        route = Screen.Home.route,
    ) {
        HomeScreen(
            snackBarHostState = snackBarHostState,
            onPictureClick = {
                navController.navigate(
                    Screen.FullImage.createRoute(
                        pictureId = it.id
                    )
                )
            }
        )
    }
}

fun NavGraphBuilder.favouritesScreen(navController: NavController) {
    composable(
        route = Screen.Favourites.route,
    ) {
        ShowFavourites {
            navController.navigate(Screen.FullImage.route)
        }
    }
}

fun NavGraphBuilder.fullScreen(snackBarHostState: SnackbarHostState) {
    composable(
        route = Screen.FullImage.route,
        arguments = Screen.FullImage.navArguments
    ) {
        FullImageScreen(
            snackBarHostState = snackBarHostState,
        )
    }
}