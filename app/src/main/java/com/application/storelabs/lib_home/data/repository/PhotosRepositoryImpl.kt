package com.application.storelabs.lib_home.data.repository

import com.application.storelabs.lib_home.domain.model.Photo
import com.application.storelabs.lib_home.domain.model.asEntity
import com.application.storelabs.lib_home.local.PhotosDao
import com.application.storelabs.lib_home.local.toDomainModel
import com.application.storelabs.lib_home.remote.PhotosNetworkDataSource
import com.application.storelabs.lib_home.remote.model.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(
    private val localDataSource: PhotosDao,
    private val networkDataSource: PhotosNetworkDataSource
) : PhotosRepository {

    override suspend fun getPhotos(): Flow<List<Photo>> = flow {
        val cachedPhotos = localDataSource.getPhotos().map { it.toDomainModel() }
        if (cachedPhotos.isNotEmpty()) {
            emit(cachedPhotos)
        } else {
            val remotePhotos = networkDataSource.getPhotos().map { it.toDomainModel() }.shuffled()
            localDataSource.refreshPhotos(photoEntity = remotePhotos.map { it.asEntity() })
            emit(value = remotePhotos)
        }
    }

    override suspend fun toggleFavourite(id: String, isFavourite: Boolean) {
        localDataSource.toggleFavourites(id = id, isFavourite = isFavourite)
    }

    override suspend fun getFavouritePhotos(): Flow<List<Photo>> = flow {
        val favouritePhotos = localDataSource.getFavouritePhotos().map { it.toDomainModel() }
        emit(favouritePhotos)
    }

}