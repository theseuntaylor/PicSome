package com.application.storelabs.feature.home.model

sealed class HomeUiState {
    object Initial : HomeUiState()
    object Loading : HomeUiState()
    class Error(val errorMessage: String) : HomeUiState()
    class Success(val data: List<PhotoUi>) : HomeUiState()
}