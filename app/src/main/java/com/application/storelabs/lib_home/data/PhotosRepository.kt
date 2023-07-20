package com.application.storelabs.lib_home.data

import com.application.storelabs.lib_home.data.model.PhotoDTO
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {
    suspend fun getPhotos(): Flow<List<PhotoDTO>>
}