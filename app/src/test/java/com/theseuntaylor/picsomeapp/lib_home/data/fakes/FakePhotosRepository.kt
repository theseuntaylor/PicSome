package com.theseuntaylor.picsomeapp.lib_home.data.fakes

import com.theseuntaylor.picsomeapp.lib_home.data.repository.PhotosRepository
import com.theseuntaylor.picsomeapp.lib_home.domain.model.Photo
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * In-memory repository whose calls can be held open with a gate, so tests can observe the
 * state while a load is still in flight.
 */
class FakePhotosRepository(var photos: List<Photo> = emptyList()) : PhotosRepository {

    /** Thrown by [getPhotos] and [getPhotoById] when set. */
    var loadError: Throwable? = null

    /** When set, [getPhotos] suspends until it completes. */
    var photosGate: CompletableDeferred<Unit>? = null

    /** What the next [loadNextPage] call does: add photos and return true, return false, or throw. */
    var nextPage: suspend () -> Boolean = { false }
    var loadNextPageCalls = 0
        private set

    /** Thrown by [toggleFavourite] when set. */
    var toggleError: Throwable? = null

    override suspend fun getPhotos(): Flow<List<Photo>> = flow {
        photosGate?.await()
        loadError?.let { throw it }
        emit(photos)
    }

    override suspend fun loadNextPage(): Boolean {
        loadNextPageCalls++
        return nextPage()
    }

    override suspend fun getPhotoById(id: String): Flow<Photo> = flow {
        loadError?.let { throw it }
        emit(photos.single { it.id == id })
    }

    override suspend fun toggleFavourite(id: String, isFavourite: Boolean) {
        toggleError?.let { throw it }
        photos = photos.map { if (it.id == id) it.copy(isFavourite = isFavourite) else it }
    }

    override suspend fun getFavouritePhotos(): Flow<List<Photo>> = flow {
        emit(photos.filter { it.isFavourite })
    }

    companion object {
        fun photo(id: String, author: String = "Author $id") = Photo(
            id = id,
            author = author,
            width = 4000,
            height = 3000,
            url = "https://unsplash.com/photos/$id",
            download_url = "https://picsum.photos/id/$id/4000/3000",
        )

        fun photos(ids: IntRange) = ids.map { photo(it.toString()) }
    }
}
