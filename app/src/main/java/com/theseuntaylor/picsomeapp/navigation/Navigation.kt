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
                    Screen.FullScreenImage.createRoute(
                        pictureId = it
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
            navController.navigate(Screen.FullScreenImage.route)
        }
    }
}

fun NavGraphBuilder.fullScreen(snackBarHostState: SnackbarHostState) {
    composable(
         route = Screen.FullScreenImage.route,
        arguments = Screen.FullScreenImage.navArguments
    ) {
        FullImageScreen(snackBarHostState = snackBarHostState)
    }
}