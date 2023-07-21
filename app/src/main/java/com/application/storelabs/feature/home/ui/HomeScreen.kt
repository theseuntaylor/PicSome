package com.application.storelabs.feature.home.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.application.storelabs.feature.home.model.HomeUiState
import com.application.storelabs.feature.home.model.PhotoUi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {

    val uiState = viewModel.uiState

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val state = uiState.value) {
            is HomeUiState.Loading -> {
                Loader()
            }

            is HomeUiState.Success -> {
                state.data.filter { it.isFavourite }
                Column {
                    Text(
                        "Your Photo Library",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        style = Typography.bodyLarge.copy(
                            fontWeight = FontWeight.W700,
                            fontSize = 24.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2)
                    ) {
                        items(items = state.data, key = { photo: PhotoUi -> photo.id }) { photo ->
                            PhotoItem(
                                photo = photo,
                                toggleFavourites = viewModel::toggleFavourite
                            )
                        }
                    }
                }
            }

            is HomeUiState.Error -> {
                state.errorMessage
            }

            else -> {}
        }
    }
}