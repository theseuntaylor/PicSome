package com.theseuntaylor.picsomeapp.core

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

    @Composable
    fun ShowErrorSnackBar(message: String, snackbarHostState: SnackbarHostState) {
        LaunchedEffect(key1 = message) {
            snackbarHostState.showSnackbar(message)
        }
    }