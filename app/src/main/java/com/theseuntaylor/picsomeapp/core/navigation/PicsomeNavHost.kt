package com.theseuntaylor.picsomeapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.theseuntaylor.picsomeapp.core.PicSomeAppState

@Composable
fun PicsomeNavHost(
    navController: NavHostController,
    appState: PicSomeAppState,
) {
    NavHost(
        navController = navController,
        startDestination = homeRoute,
        modifier = Modifier
    ) {
        homeScreen(
            onScrollDirectionChanged = appState::onScrollDirectionChanged,
            onPhotoClicked = { photoId ->
                navController.navigate("photo_details/$photoId")
            }
        )
        photoDetailsScreen(
            navController = navController
        )
        favouritesScreen(
            onPhotoClicked = { photoId ->
                navController.navigate("photo_details/$photoId")
            }
        )
    }
}
