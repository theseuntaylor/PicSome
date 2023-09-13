package com.theseuntaylor.picsomeapp.lib_home.data.repository

import com.theseuntaylor.picsomeapp.core.transformException
import com.theseuntaylor.picsomeapp.lib_home.domain.model.Photo
import com.theseuntaylor.picsomeapp.lib_home.domain.model.asEntity
import com.theseuntaylor.picsomeapp.lib_home.local.PhotosDao
import com.theseuntaylor.picsomeapp.lib_home.local.toDomainModel
import com.theseuntaylor.picsomeapp.lib_home.remote.PhotosNetworkDataSource
import com.theseuntaylor.picsomeapp.lib_home.remote.model.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.net.ssl.SSLException
import kotlin.jvm.Throws

class PhotosRepositoryImpl @Inject constructor(
    private val localDataSource: PhotosDao,
    private val networkDataSource: PhotosNetworkDataSource,
) : PhotosRepository {

    @Throws(SSLException::class)
    override suspend fun getPhotos(): Flow<List<Photo>> = flow {
        try {
            val cachedPhotos = localDataSource.getPhotos().map { it.toDomainModel() }
            if (cachedPhotos.isNotEmpty()) {
                emit(cachedPhotos)
            } else {
//                if (networkDataSource.getPhotos().isSuccessful) {
                val remotePhotos =
                    networkDataSource.getPhotos().map { it.toDomainModel() }.shuffled()
                remotePhotos?.map { it.asEntity() }
                    ?.let { localDataSource.refreshPhotos(photoEntity = it) }
                if (remotePhotos != null) {
                    emit(value = remotePhotos)
                }

//                }
            }
        } catch (e: Exception) {
            e.localizedMessage
            throw e.transformException()
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