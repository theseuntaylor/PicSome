package com.theseuntaylor.picsomeapp.feature.detail.model

import com.theseuntaylor.picsomeapp.feature.home.model.PhotoUi

sealed class PhotoDetailsUiState {
    object Initial : PhotoDetailsUiState()
    object Loading : PhotoDetailsUiState()
    data class Success(val photo: PhotoUi) : PhotoDetailsUiState()
    data class Error(val errorMessage: String) : PhotoDetailsUiState()
}