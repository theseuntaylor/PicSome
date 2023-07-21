package com.application.storelabs.feature.favourites.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.storelabs.core.components.Loader
import com.application.storelabs.core.components.PhotoItem
import com.application.storelabs.core.theme.Typography
import com.application.storelabs.feature.favourites.FavouritesUiState
import com.application.storelabs.feature.home.model.PhotoUI

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowFavourites(viewModel: FavouriteViewModel = hiltViewModel()) {

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
                        "Your Favourite Photos",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        style = Typography.bodyLarge.copy(
                            fontWeight = FontWeight.W700,
                            fontSize = 24.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                    if (favouritePhotos.isEmpty()) {
                        Text(
                            "Your Favourite Photos",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp),
                            style = Typography.bodyLarge.copy(
                                fontWeight = FontWeight.W400,
                                fontSize = 18.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(2)
                        ) {
                            items(
                                items = state.favouritePhotos,
                                key = { photo: PhotoUI -> photo.id }) { photo ->
                                PhotoItem(
                                    photo = photo,
                                    toggleFavourites = viewModel::toggleFavourite
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
                            .padding(5.dp),
                        style = Typography.bodyLarge.copy(
                            fontWeight = FontWeight.W400,
                            fontSize = 18.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {}
        }
    }
}