package com.theseuntaylor.picsomeapp.feature.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.theseuntaylor.picsomeapp.core.ShowErrorSnackBar
import com.theseuntaylor.picsomeapp.core.components.Loader
import com.theseuntaylor.picsomeapp.core.components.PhotoItem
import com.theseuntaylor.picsomeapp.core.theme.Typography
import com.theseuntaylor.picsomeapp.feature.home.model.HomeUiState
import com.theseuntaylor.picsomeapp.feature.home.model.PhotoUi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState,
    onScrollDirectionChanged: (Boolean) -> Unit
) {

    val listState = rememberLazyStaggeredGridState()
    val uiState = viewModel.uiState

    var previousScrollOffset by remember { mutableStateOf(0) }

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
                            fontWeight = FontWeight.W700, fontSize = 24.sp,
                            color = colorScheme.primary
                        ),
                        textAlign = TextAlign.Center
                    )
                    LazyVerticalStaggeredGrid(
                        state = listState,
                        columns = StaggeredGridCells.Fixed(2)
                    ) {
                        items(items = state.data, key = { photo: PhotoUi -> photo.id }) { photo ->
                            PhotoItem(
                                photo = photo, toggleFavourites = viewModel::toggleFavourite
                            )
                        }
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

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset }
            .map { currentOffset ->
                val scrollingUp = currentOffset < previousScrollOffset
                previousScrollOffset = currentOffset
                scrollingUp || currentOffset == 0
            }
            .distinctUntilChanged()
            .collect { isVisible ->
                onScrollDirectionChanged(isVisible)
            }
    }
}