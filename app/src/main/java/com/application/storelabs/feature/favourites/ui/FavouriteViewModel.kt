package com.application.storelabs.feature.favourites.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.storelabs.feature.favourites.FavouritesUiState
import com.application.storelabs.feature.home.model.asUiModel
import com.application.storelabs.lib_home.data.repository.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val repository: PhotosRepository
) : ViewModel() {

    private val _uiState = mutableStateOf<FavouritesUiState>(FavouritesUiState.Initial)
    val uiState: State<FavouritesUiState> = _uiState

    fun getFavouritePhotos() = viewModelScope.launch {
        repository.getFavouritePhotos()
            .map { photos ->
                photos.map { it.asUiModel() }
            }
            .collect { favouritePhotos ->
                _uiState.value = FavouritesUiState.Success(favouritePhotos)
            }
    }

    fun toggleFavourite(id: String, isFavourite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavourite(id, isFavourite)
            repository.getFavouritePhotos()
                .map { photos ->
                    photos.map { it.asUiModel() }
                }
                .collect { photosUI ->
                    _uiState.value = FavouritesUiState.Success(photosUI)
                }
        }
    }
}