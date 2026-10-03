package com.theseuntaylor.picsomeapp.feature.home.model

sealed class HomeUiState {
    object Initial : HomeUiState()
    object Loading : HomeUiState()
    data class Error(val errorMessage: String) : HomeUiState()
    data class Success(
        val data: List<PhotoUi>,
        val isLoadingMore: Boolean = false,
        val endReached: Boolean = false,
        val loadMoreError: String? = null,
    ) : HomeUiState()
}