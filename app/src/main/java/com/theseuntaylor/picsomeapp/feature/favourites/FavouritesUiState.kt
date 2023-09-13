package com.theseuntaylor.picsomeapp.feature.favourites

import com.theseuntaylor.picsomeapp.feature.home.model.PhotoUi

sealed class FavouritesUiState {
    object Initial : FavouritesUiState()
    object Loading : FavouritesUiState()
    data class Error(val errorMessage: String) : FavouritesUiState()
    data class Success(val favouritePhotos: List<PhotoUi>) : FavouritesUiState()
}