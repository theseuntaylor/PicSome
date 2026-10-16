package com.theseuntaylor.picsomeapp.feature.favourites.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.theseuntaylor.picsomeapp.R
import com.theseuntaylor.picsomeapp.core.components.Loader
import com.theseuntaylor.picsomeapp.core.components.PhotoItem
import com.theseuntaylor.picsomeapp.feature.favourites.FavouritesUiState
import com.theseuntaylor.picsomeapp.feature.home.model.PhotoUi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowFavourites(
    viewModel: FavouriteViewModel = hiltViewModel(),
    onPhotoClicked: (String) -> Unit
) {

    LaunchedEffect(Unit) {
        viewModel.getFavouritePhotos()
    }

    val uiState = viewModel.uiState

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (val state = uiState.value) {
            is FavouritesUiState.Loading -> {
                Loader()
            }

            is FavouritesUiState.Success -> {

                val favouritePhotos = state.favouritePhotos

                Column {
                    Text(
                        stringResource(R.string.favourites_title),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.headlineSmall,
                        color = colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    if (favouritePhotos.isEmpty()) {
                        Text(
                            stringResource(R.string.favourites_empty),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(2)
                        ) {
                            items(
                                items = state.favouritePhotos,
                                key = { photo: PhotoUi -> photo.id }) { photo ->
                                PhotoItem(
                                    photo = photo,
                                    toggleFavourites = viewModel::toggleFavourite,
                                    onPhotoClicked = { photoId ->
                                        onPhotoClicked(photoId)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            is FavouritesUiState.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        state.errorMessage,
                        modifier = Modifier
                            .padding(24.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {}
        }
    }
}