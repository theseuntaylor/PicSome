package com.theseuntaylor.picsomeapp.lib_home.data

import app.cash.turbine.test
import com.theseuntaylor.picsomeapp.core.transformException
import com.theseuntaylor.picsomeapp.lib_home.data.fakes.DummyData
import com.theseuntaylor.picsomeapp.lib_home.data.repository.PhotosRepositoryImpl
import com.theseuntaylor.picsomeapp.lib_home.local.PhotosDao
import com.theseuntaylor.picsomeapp.lib_home.remote.PhotosNetworkDataSource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import javax.net.ssl.SSLException

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
        favouritePhotos.collect { actualFavouritePhotos ->
            assert(actualFavouritePhotos == expectedFavouritePhotos)
        }
    }

    @Test(expected = Exception::class)
    fun `when favourite photos failed, then we get the error message`() = runTest {
        // given
        val localDataSource = mock<PhotosDao>().apply {
            whenever(getFavouritePhotos()).thenAnswer { throw Exception(ERROR_MSG) }
        }
        val photosRepository = createPhotosRepository(localDataSource = localDataSource)
        // when
        photosRepository.getFavouritePhotos().collect {}
    }

    @Test
    fun `when getting photos from remote fails, then we get unknown error message`() = runTest {
        // given
        val networkDataSource = mock<PhotosNetworkDataSource>().apply {
            whenever(getPhotos()).thenAnswer { throw Throwable(ERROR_MSG) }
        }
        val photosRepository = createPhotosRepository(
            localDataSource = emptyCache(),
            networkDataSource = networkDataSource
        )
        // when
        val favouritePhotos = photosRepository.getPhotos()
        // then
        favouritePhotos.test {
            assert(awaitError().message == ERROR_MSG)
        }
    }

    @Test
    fun `when getting photos from remote fails, then we get known error message`() = runTest {
        // given
        val networkDataSource = mock<PhotosNetworkDataSource>().apply {
            whenever(getPhotos()).thenAnswer { throw SSLException("handshake failed") }
        }
        val photosRepository = createPhotosRepository(
            localDataSource = emptyCache(),
            networkDataSource = networkDataSource
        )
        // when
        val favouritePhotos = photosRepository.getPhotos()
        // then

        favouritePhotos.test {
            assert(awaitError().message == EXPECTED_ERROR_MSG)
        }
    }

    @Test
    fun `when favourite photos is toggled, then the database is called`() = runTest {
        // given
        val localDataSource = mock<PhotosDao>().apply {
            whenever(
                toggleFavourites(
                    id = any(), isFavourite = any()
                )
            ).thenReturn(
                Unit
            )
        }
        val photosRepository = createPhotosRepository(localDataSource = localDataSource)
        // when
        photosRepository.toggleFavourite(
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
        val localDataSource = mock<PhotosDao>().apply {
            whenever(
                toggleFavourites(
                    id = any(), isFavourite = any()
                )
            ).thenReturn(
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

    @Test
    fun `when fetching photos, then we fetch from cached source first`() = runTest {
        // given
        val expectedPhotos = DummyData.allPhotos
        val localDataSource = mock<PhotosDao>().apply {
            whenever(getPhotos()).thenReturn(
                DummyData.cachedPhotos
            )
        }
        val photosRepository = createPhotosRepository(localDataSource = localDataSource)
        // when
        val photos = photosRepository.getPhotos().catch {
            it.transformException()
        }
        // then
        photos.collect { actualPhotos ->
            assert(actualPhotos == expectedPhotos)
        }
    }

    @Test
    fun `when fetching photos and cache source is empty, then we fetch from remote source`() =
        runTest {
            // given
            val expectedPhotos = DummyData.allPhotosFromRemote.sortedBy { it.id }
            val localDataSource = mock<PhotosDao>().apply {
                whenever(getPhotos()).thenReturn(
                    emptyList()
                )
                whenever(refreshPhotos(any())).thenReturn(
                    Unit
                )
            }
            val networkDataSource = mock<PhotosNetworkDataSource>().apply {
                whenever(getPhotos()).thenReturn(
                    DummyData.remotePhotos
                )
            }
            val photosRepository = createPhotosRepository(
                localDataSource = localDataSource,
                networkDataSource = networkDataSource,
            )
            // when
            val photos = photosRepository.getPhotos()
            // then
            photos.collect { actualPhotos ->
                assert(actualPhotos.sortedBy { it.id } == expectedPhotos)
            }
        }

    // endregion


    // region private helpers
    val ERROR_MSG = "Something went wrong!"
    val EXPECTED_ERROR_MSG = "There seems to be no network connection"

    private suspend fun emptyCache() = mock<PhotosDao>().apply {
        whenever(getPhotos()).thenReturn(emptyList())
    }

    private fun createPhotosRepository(
        localDataSource: PhotosDao = mock(),
        networkDataSource: PhotosNetworkDataSource = mock(),
    ) = PhotosRepositoryImpl(
        localDataSource = localDataSource,
        networkDataSource = networkDataSource,
    )
    // endregion
}