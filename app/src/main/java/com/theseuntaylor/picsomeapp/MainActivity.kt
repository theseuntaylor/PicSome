package com.theseuntaylor.picsomeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.theseuntaylor.picsomeapp.core.PicSomeAppState
import com.theseuntaylor.picsomeapp.core.theme.PickSomeApplicationTheme
import com.theseuntaylor.picsomeapp.navigation.BottomAppBar
import com.theseuntaylor.picsomeapp.navigation.Screen
import com.theseuntaylor.picsomeapp.navigation.favouritesScreen
import com.theseuntaylor.picsomeapp.navigation.fullScreen
import com.theseuntaylor.picsomeapp.navigation.homeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PickSomeApplicationTheme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }
                val appState = PicSomeAppState(navController)

                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            if (appState.shouldShowBottomBar){
                                BottomAppBar(
                                    currentRoute = appState.currentDestination,
                                    destinations = appState.topLevelDestinations,
                                    navigateToDestinations = appState::navigateToTopDestinations
                                )
                            }
                        },
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState)
                        }
                    ) { padding ->
                        Row(
                            Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            Column(Modifier.fillMaxSize()) {
                                NavHost(
                                    navController = navController,
                                    startDestination = Screen.Home.route,
                                    modifier = Modifier
                                ) {
                                    homeScreen(
                                        snackBarHostState = snackbarHostState,
                                        navController = navController
                                    )
                                    favouritesScreen(navController = navController)
                                    navigation(
                                        route = "fullScreenRoute",
                                        startDestination = Screen.FullScreenImage.route
                                    ) {
                                        fullScreen(snackBarHostState = snackbarHostState)
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}