package com.theseuntaylor.picsomeapp.feature.detail.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.theseuntaylor.picsomeapp.R
import com.theseuntaylor.picsomeapp.core.components.Loader
import com.theseuntaylor.picsomeapp.feature.detail.model.PhotoDetailsUiState
import com.theseuntaylor.picsomeapp.feature.home.model.aspectRatio
import com.theseuntaylor.picsomeapp.feature.home.model.sizedUrl
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailsScreen(
    viewModel: PhotoDetailsViewModel = hiltViewModel(),
    onUpClick: () -> Unit,
) {
    val uiState = viewModel.uiState.value
    val downloadMessage = viewModel.downloadMessage.value
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarScope = rememberCoroutineScope()
    val context = LocalContext.current

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) viewModel.downloadPhoto() else viewModel.onStoragePermissionDenied()
    }

    val onDownloadClick = {
        val needsStoragePermission = Build.VERSION.SDK_INT <= Build.VERSION_CODES.P &&
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
        if (needsStoragePermission) {
            storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            viewModel.downloadPhoto()
        }
    }

    val downloadMessageText = downloadMessage?.let { stringResource(it) }
    LaunchedEffect(downloadMessage) {
        if (downloadMessageText != null) {
            // Clearing the message recomposes and cancels this effect, so show it from a
            // scope that outlives it.
            snackbarScope.launch { snackbarHostState.showSnackbar(downloadMessageText) }
            viewModel.onDownloadMessageShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.photo_details)) },
                navigationIcon = {
                    IconButton(onClick = onUpClick) {
                        Icon(
                            painterResource(R.drawable.ic_arrow_back_24),
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onDownloadClick,
                        enabled = uiState is PhotoDetailsUiState.Success
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_arrow_download_24),
                            contentDescription = stringResource(R.string.download)
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
                    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = uiState.photo.sizedUrl(targetWidthPx = constraints.maxWidth),
                            contentDescription = "Photo by ${uiState.photo.author}",
                            placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(uiState.photo.aspectRatio)
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
