package com.theseuntaylor.picsomeapp.feature.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.theseuntaylor.picsomeapp.R
import com.theseuntaylor.picsomeapp.core.components.Loader
import com.theseuntaylor.picsomeapp.feature.detail.model.PhotoDetailsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailsScreen(
    viewModel: PhotoDetailsViewModel = hiltViewModel(),
    onUpClick: () -> Unit,
) {
    val uiState = viewModel.uiState.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Photo Details") },
                navigationIcon = {
                    IconButton(onClick = onUpClick) {
                        Icon(
                            painterResource(R.drawable.ic_arrow_back_24),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.downloadPhoto() }) {
                        Icon(
                            painterResource(R.drawable.ic_arrow_download_24),
                            contentDescription = "Download"
                        )
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            when (uiState) {
                is PhotoDetailsUiState.Loading -> {
                    Loader()
                }

                is PhotoDetailsUiState.Success -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = uiState.photo.download_url,
                            contentDescription = "Photo by ${uiState.photo.author}"
                        )
                    }
                }

                is PhotoDetailsUiState.Error -> {
                    Text(text = uiState.errorMessage)
                }

                else -> {}
            }
        }
    }
}