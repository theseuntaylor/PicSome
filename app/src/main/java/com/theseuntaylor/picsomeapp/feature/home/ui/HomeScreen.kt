package com.theseuntaylor.picsomeapp.feature.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.theseuntaylor.picsomeapp.core.ShowErrorSnackBar
import com.theseuntaylor.picsomeapp.core.components.Loader
import com.theseuntaylor.picsomeapp.core.components.PhotoItem
import com.theseuntaylor.picsomeapp.core.theme.Typography
import com.theseuntaylor.picsomeapp.feature.home.model.HomeUiState
import com.theseuntaylor.picsomeapp.feature.home.model.PhotoUi

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState,
    onPictureClick: (String) -> Unit,
) {

    val uiState = viewModel.uiState

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Your Photo Library",
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            style = Typography.bodyLarge.copy(
                fontWeight = FontWeight.W700, fontSize = 24.sp
            ),
            textAlign = TextAlign.Center
        )
        when (val state = uiState.value) {
            is HomeUiState.Loading -> {
                Loader()
            }

            is HomeUiState.Success -> {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2)
                ) {
                    items(items = state.data, key = { photo: PhotoUi -> photo.id }) { photo ->
                        PhotoItem(
                            photo = photo,
                            toggleFavourites = viewModel::toggleFavourite,
                            navigateToFullScreen = { onPictureClick(photo.id) }
                        )
                    }
                }
            }

            is HomeUiState.Error -> {
                ShowErrorSnackBar(
                    message = state.errorMessage, snackbarHostState = snackBarHostState
                )
            }

            else -> {}
        }
    }
}