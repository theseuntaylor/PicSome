package com.application.storelabs.feature.favourites

import com.application.storelabs.feature.home.model.PhotoUi

sealed class FavouritesUiState {
    object Initial : FavouritesUiState()
    object Loading : FavouritesUiState()
    class Error(val errorMessage: String) : FavouritesUiState()
    class Success(val favouritePhotos: List<PhotoUi>) : FavouritesUiState()
}