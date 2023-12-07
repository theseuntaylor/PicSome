package com.theseuntaylor.picsomeapp.feature.view_full_image.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FullImageScreen(
    viewModel: FullScreenViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState,
) {
    Text(text = "This is the Full Screen Page")
}