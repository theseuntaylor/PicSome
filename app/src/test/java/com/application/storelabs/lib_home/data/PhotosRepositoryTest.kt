package com.application.storelabs.lib_home.data

import com.application.storelabs.lib_home.data.fakes.DummyData
import com.application.storelabs.lib_home.data.repository.PhotosRepositoryImpl
import com.application.storelabs.lib_home.local.PhotosDao
import com.application.storelabs.lib_home.remote.PhotosNetworkDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class PhotosRepositoryTest {


    // region Tests
    @Test
    fun `when favourite photos is fetched, then we get the photos successfully`() = runTest {
        // given
        val expectedFavouritePhotos = DummyData.favouritePhotos
        val localDataSource = mock<PhotosDao>().apply {
            whenever(getFavouritePhotos()).thenReturn(
                DummyData.cachedFavouritePhotos
            )
        }
        val photosRepository = createPhotosRepository(localDataSource = localDataSource)
        // when
        val favouritePhotos = photosRepository.getFavouritePhotos()
        // then
        favouritePhotos.collect{actualFavouritePhotos ->
            assert(actualFavouritePhotos == expectedFavouritePhotos)
        }
    }

    @Test(expected = Exception::class)
    fun `when favourite photos failed, then we get the error message`() = runTest {
        // given
        val localDataSource = mock<PhotosDao>().apply {
            whenever(getFavouritePhotos()).thenReturn(
               throw Exception(ERROR_MSG)
            )
        }
        val photosRepository = createPhotosRepository(localDataSource = localDataSource)
        // when
        val favouritePhotos = photosRepository.getFavouritePhotos()
    }

    @Test
    fun `when favourite photos is toggled, then the database is called`() = runTest {
        // given
        DummyData.favouritePhotos
        val localDataSource = mock<PhotosDao>().apply {
            whenever(toggleFavourites(
                id = any(), isFavourite = any()
            )).thenReturn(
                Unit
            )
        }
        val photosRepository = createPhotosRepository(localDataSource = localDataSource)
        // when
        val toggleFavourite = photosRepository.toggleFavourite(
            id = "1", isFavourite = true
        )
        // then
        verify(localDataSource).toggleFavourites(
            id = "1", isFavourite = true,
        )
    }

    @Test
    fun `when favourite photos is toggled, then unit is returned`() = runTest {
        // given
        DummyData.favouritePhotos
        val localDataSource = mock<PhotosDao>().apply {
            whenever(toggleFavourites(
                id = any(), isFavourite = any()
            )).thenReturn(
                Unit
            )
        }
        val photosRepository = createPhotosRepository(localDataSource = localDataSource)
        // when
        val toggleFavourite = photosRepository.toggleFavourite(
            id = "1", isFavourite = true
        )
        // then
        assert(toggleFavourite == Unit)
    }



    // endregion


    // region private helpers
    val ERROR_MSG = "Something went wrong!"
    private fun createPhotosRepository(
        localDataSource: PhotosDao = mock(),
        networkDataSource: PhotosNetworkDataSource = mock()
    ) = PhotosRepositoryImpl(
        localDataSource = localDataSource,
        networkDataSource = networkDataSource,
    )
    // endregion
}