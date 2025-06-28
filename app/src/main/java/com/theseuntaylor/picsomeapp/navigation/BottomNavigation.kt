package com.theseuntaylor.picsomeapp.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.theseuntaylor.picsomeapp.R

enum class Destinations(
    val destinationRouteName: String,
    @StringRes val destinationName: Int,
    @DrawableRes val icon: Int,
    @StringRes val contentDescription: Int
) {
    HOME(
        homeRoute,
        R.string.home,
        R.drawable.ic_home_24,
        R.string.home_content_description
    ),
    FAVOURITES(
        favouritesRoute,
        R.string.favourites,
        R.drawable.ic_favorite_24,
        R.string.favourites_content_description
    )
}

@Composable
fun BottomAppBar(
    navigateToDestinations: (Destinations) -> Unit,
    currentRoute: NavDestination?,
    destinations: List<Destinations>
) {

    NavigationBar(
        contentColor = colorScheme.surface,
    ) {
        destinations.forEach { destination ->

            val selected = currentRoute.isTopLevelDestinationInHierarchy(destination)

            NavigationBarItem(
                selected = (selected),
                onClick = { navigateToDestinations(destination) },
                icon = {
                    Icon(
                        painter = painterResource(destination.icon),
                        contentDescription = stringResource(
                            id = destination.contentDescription
                        )
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = destination.destinationName)
                    )
                }
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: Destinations) =
    this?.hierarchy?.any {
        it.route?.contains(destination.destinationRouteName, true) ?: false
    } ?: false