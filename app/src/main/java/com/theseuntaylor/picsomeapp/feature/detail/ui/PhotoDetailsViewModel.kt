package com.theseuntaylor.picsomeapp.feature.detail.ui

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theseuntaylor.picsomeapp.R
import com.theseuntaylor.picsomeapp.core.downloader.Downloader
import com.theseuntaylor.picsomeapp.feature.detail.model.PhotoDetailsUiState
import com.theseuntaylor.picsomeapp.feature.home.model.asUiModel
import com.theseuntaylor.picsomeapp.lib_home.data.repository.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: PhotosRepository,
    private val downloader: Downloader,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _uiState = mutableStateOf<PhotoDetailsUiState>(PhotoDetailsUiState.Initial)
    val uiState: State<PhotoDetailsUiState> = _uiState

    private val _downloadMessage = mutableStateOf<Int?>(null)

    /** One-off feedback for the last download attempt; cleared via [onDownloadMessageShown]. */
    val downloadMessage: State<Int?> = _downloadMessage

    init {
        savedStateHandle.get<String>("photoId")?.let { photoId ->
            getPhoto(photoId)
        }
    }

    private fun getPhoto(id: String) = viewModelScope.launch {
        repository.getPhotoById(id)
            .onStart { _uiState.value = PhotoDetailsUiState.Loading }
            .catch {
                _uiState.value =
                    PhotoDetailsUiState.Error(it.message ?: "There was an error loading image")
            }
            .map { photo ->
                photo.asUiModel()
            }
            .collect { photoUi ->
                _uiState.value = PhotoDetailsUiState.Success(photoUi)
            }
    }

    fun downloadPhoto() {
        val photo = (uiState.value as? PhotoDetailsUiState.Success)?.photo ?: return
        _downloadMessage.value = try {
            downloader.downloadFile(
                url = photo.download_url,
                fileName = "picsome_${photo.id}.jpg",
                title = context.getString(R.string.download_title, photo.author),
            )
            R.string.download_started
        } catch (e: Exception) {
            // DownloadManager throws if storage is unavailable or permission was revoked.
            R.string.download_failed
        }
    }

    fun onStoragePermissionDenied() {
        _downloadMessage.value = R.string.download_needs_storage_permission
    }

    fun onDownloadMessageShown() {
        _downloadMessage.value = null
    }
}