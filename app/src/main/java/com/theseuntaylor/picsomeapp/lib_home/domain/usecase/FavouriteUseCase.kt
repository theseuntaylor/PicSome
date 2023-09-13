package com.theseuntaylor.picsomeapp.lib_home.domain.usecase

import com.theseuntaylor.picsomeapp.lib_home.data.repository.PhotosRepository
import com.theseuntaylor.picsomeapp.lib_home.domain.model.Photo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavouriteUseCase @Inject constructor(
    private val photosRepository: PhotosRepository
) {
    suspend fun execute() = photosRepository.getFavouritePhotos()
}

fun interface FavouritesFunctionalUseCase: suspend () -> Flow<List<Photo>>