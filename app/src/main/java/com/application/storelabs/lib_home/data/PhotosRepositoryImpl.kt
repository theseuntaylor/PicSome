package com.application.storelabs.lib_home.data

import com.application.storelabs.di.AppConstants
import com.application.storelabs.lib_home.data.model.PhotoDTO
import com.application.storelabs.lib_home.local.PhotosDao
import com.application.storelabs.lib_home.remote.PhotosNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class PhotosRepositoryImpl @Inject constructor(
    private val localDataSource: PhotosDao,
    private val networkDataSource: PhotosNetworkDataSource,
    @Named(AppConstants.IO_DISPATCHER) private val dispatcher: CoroutineDispatcher,
    private val scope: CoroutineScope
) : PhotosRepository {

    override suspend fun getPhotos(): Flow<List<PhotoDTO>> = flow {
        emit(networkDataSource.getPhotos())
    }

}