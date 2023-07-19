package com.application.storelabs

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.application.storelabs.core.BottomAppBar
import com.application.storelabs.core.Destinations
import com.application.storelabs.core.StoreLabAppState
import com.application.storelabs.navigation.favouritesScreen
import com.application.storelabs.navigation.homeRoute
import com.application.storelabs.navigation.homeScreen
import com.application.storelabs.ui.theme.StoreLabsApplicationTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StoreLabsApplicationTheme {

                val navController = rememberNavController()
                val appState = StoreLabAppState(navController)

                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            BottomAppBar(
                                navigateToDestinations = appState::navigateToTopDestinations,
                                navController = navController,
                                currentRoute = appState.currentDestination,
                                destinations = Destinations.values().asList()
                            )
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
                                    startDestination = homeRoute,
                                    modifier = Modifier
                                ) {
                                    homeScreen()
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}