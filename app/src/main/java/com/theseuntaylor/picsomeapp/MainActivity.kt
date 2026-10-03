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
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.theseuntaylor.picsomeapp.core.components.LocalPhotoImageUrls
import com.theseuntaylor.picsomeapp.core.navigation.BottomAppBar
import com.theseuntaylor.picsomeapp.core.navigation.PicsomeNavHost
import com.theseuntaylor.picsomeapp.core.rememberPicSomeAppState
import com.theseuntaylor.picsomeapp.core.theme.PickSomeApplicationTheme
import com.theseuntaylor.picsomeapp.core.theme.ProvideWindowInsetsController
import com.theseuntaylor.picsomeapp.lib_home.domain.model.PhotoImageUrls
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var photoImageUrls: PhotoImageUrls

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PickSomeApplicationTheme {
                CompositionLocalProvider(LocalPhotoImageUrls provides photoImageUrls) {

                    ProvideWindowInsetsController()

                    val navController = rememberNavController()
                    val appState = rememberPicSomeAppState(navController)

                    Surface(
                        modifier = Modifier.fillMaxSize(), color = Color.Transparent,
                    ) {
                        Scaffold(
                            bottomBar = {
                                AnimatedVisibility(
                                    visible = appState.shouldShowBottomBar,
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
                        ) { padding ->
                            Row(
                                Modifier
                                    .fillMaxSize()
                                    .padding(padding)
                            ) {
                                Column(Modifier.fillMaxSize()) {
                                    PicsomeNavHost(
                                        navController = navController,
                                        appState = appState,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}