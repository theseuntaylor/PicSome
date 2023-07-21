package com.application.storelabs.feature.home.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.storelabs.di.AppConstants
import com.application.storelabs.feature.home.model.HomeUiState
import com.application.storelabs.feature.home.model.asUiModel
import com.application.storelabs.lib_home.data.repository.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PhotosRepository,
    @Named(AppConstants.IO_DISPATCHER) private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = mutableStateOf<HomeUiState>(HomeUiState.Initial)
    val uiState: State<HomeUiState> = _uiState

    init {
        getPhotos()
    }

    private fun getPhotos() = viewModelScope.launch {
        withContext(dispatcher) {
            repository.getPhotos()
                .onStart { _uiState.value = HomeUiState.Loading }
                .map { photos ->
                    photos.map { it.asUiModel() }
                }
                .collect { photosUI ->
                    _uiState.value = HomeUiState.Success(photosUI)
                }
        }
    }

    fun toggleFavourite(id: String, isFavourite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavourite(id, isFavourite)
            repository.getPhotos()
                .map { photos ->
                    photos.map { it.asUiModel() }
                }
                .collect { photosUI ->
                    _uiState.value = HomeUiState.Success(photosUI)
                }
        }

    }
}