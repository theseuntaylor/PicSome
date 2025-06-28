package com.theseuntaylor.picsomeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.theseuntaylor.picsomeapp.core.PicSomeAppState
import com.theseuntaylor.picsomeapp.core.theme.PickSomeApplicationTheme
import com.theseuntaylor.picsomeapp.core.theme.ProvideWindowInsetsController
import com.theseuntaylor.picsomeapp.navigation.BottomAppBar
import com.theseuntaylor.picsomeapp.navigation.favouritesScreen
import com.theseuntaylor.picsomeapp.navigation.homeRoute
import com.theseuntaylor.picsomeapp.navigation.homeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PickSomeApplicationTheme {

                ProvideWindowInsetsController()

                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }
                val appState = PicSomeAppState(navController)

                var bottomBarVisible by remember { mutableStateOf(true) }

                Surface(
                    modifier = Modifier.fillMaxSize(), color = Color.Transparent,
                ) {
                    Scaffold(
                        bottomBar = {
                            AnimatedVisibility(
                                visible = bottomBarVisible,
                                enter = slideInVertically(initialOffsetY = { it }),
                                exit = slideOutVertically(targetOffsetY = { it })
                            ) {
                                BottomAppBar(
                                    navigateToDestinations = appState::navigateToTopDestinations,
                                    currentRoute = appState.currentDestination,
                                    destinations = appState.topLevelDestinations
                                )
                            }
                        },
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState)
                        },
                    ) { padding ->
                        Row(
                            Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            Column(Modifier.fillMaxSize()) {
                                NavHost(
                                    navController = navController,
                                    startDestination = homeRoute,
                                    modifier = Modifier
                                ) {
                                    homeScreen(
                                        snackBarHostState = snackbarHostState,
                                        onScrollDirectionChanged = { isVisible ->
                                            bottomBarVisible = isVisible
                                        }
                                    )
                                    favouritesScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}