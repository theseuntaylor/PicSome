package com.theseuntaylor.picsomeapp.lib_home.data

import com.theseuntaylor.picsomeapp.lib_home.data.repository.PhotosRepositoryImpl
import com.theseuntaylor.picsomeapp.lib_home.local.PhotoEntity
import com.theseuntaylor.picsomeapp.lib_home.local.PhotosDao
import com.theseuntaylor.picsomeapp.lib_home.remote.PhotosNetworkDataSource
import com.theseuntaylor.picsomeapp.lib_home.remote.PhotosNetworkDataSource.Companion.PAGE_SIZE
import com.theseuntaylor.picsomeapp.lib_home.remote.model.PhotoDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.net.UnknownHostException

/** [PhotosRepositoryImpl.loadNextPage]. Each test guards one failure mode. */
class PhotosRepositoryPagingTest {

    private val dao = mock<PhotosDao>()
    private val network = mock<PhotosNetworkDataSource>()
    private val repository = PhotosRepositoryImpl(localDataSource = dao, networkDataSource = network)

    @Test
    fun `requests the page after the highest one already saved`() = runTest {
        whenever(dao.getLastLoadedPage()).thenReturn(4)
        whenever(network.getPhotos(5, PAGE_SIZE)).thenReturn(dtos("a", "b"))

        repository.loadNextPage()

        verify(network).getPhotos(5, PAGE_SIZE)
    }

    @Test
    fun `requests page 2 after the first page`() = runTest {
        whenever(dao.getLastLoadedPage()).thenReturn(1)
        whenever(network.getPhotos(2, PAGE_SIZE)).thenReturn(dtos("a"))

        repository.loadNextPage()

        verify(network).getPhotos(2, PAGE_SIZE)
    }

    @Test
    fun `saves every photo under its page, numbered from the top so they sort after earlier pages`() = runTest {
        whenever(dao.getLastLoadedPage()).thenReturn(1)
        whenever(network.getPhotos(2, PAGE_SIZE)).thenReturn(dtos("a", "b", "c"))

        assertTrue(repository.loadNextPage())

        val saved = argumentCaptor<List<PhotoEntity>>().apply { verify(dao).addPhotos(capture()) }.firstValue
        assertEquals(setOf("a", "b", "c"), saved.map { it.id }.toSet())
        assertEquals(listOf(2, 2, 2), saved.map { it.page })
        assertEquals(listOf(0, 1, 2), saved.map { it.position })
        assertTrue(saved.none { it.isFavourite })
    }

    @Test
    fun `an empty page reports no more photos and saves nothing`() = runTest {
        whenever(dao.getLastLoadedPage()).thenReturn(10)
        whenever(network.getPhotos(11, PAGE_SIZE)).thenReturn(emptyList())

        assertFalse(repository.loadNextPage())

        verify(dao, never()).addPhotos(any())
    }

    @Test
    fun `going offline reports the no-connection message and saves nothing`() = runTest {
        whenever(dao.getLastLoadedPage()).thenReturn(1)
        whenever(network.getPhotos(2, PAGE_SIZE)).thenAnswer { throw UnknownHostException("picsum.photos") }

        try {
            repository.loadNextPage()
            fail("Expected loadNextPage to throw")
        } catch (e: Throwable) {
            assertEquals("There seems to be no network connection", e.message)
        }
        verify(dao, never()).addPhotos(any())
    }

    private fun dtos(vararg ids: String) = ids.map {
        PhotoDto(
            id = it,
            author = "Author $it",
            width = 4000,
            height = 3000,
            url = "https://unsplash.com/photos/$it",
            download_url = "https://picsum.photos/id/$it/4000/3000",
        )
    }
}
