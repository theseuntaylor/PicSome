package com.theseuntaylor.picsomeapp.lib_home.data.repository

import com.theseuntaylor.picsomeapp.lib_home.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {
    suspend fun getPhotos(): Flow<List<Photo>>

    suspend fun toggleFavourite(id:String, isFavourite: Boolean)

    suspend fun getFavouritePhotos(): Flow<List<Photo>>
}