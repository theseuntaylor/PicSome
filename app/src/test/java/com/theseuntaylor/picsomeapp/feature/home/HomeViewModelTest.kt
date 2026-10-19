package com.theseuntaylor.picsomeapp.feature.home

import com.theseuntaylor.picsomeapp.core.MainDispatcherRule
import com.theseuntaylor.picsomeapp.feature.home.model.HomeUiState
import com.theseuntaylor.picsomeapp.feature.home.ui.HomeViewModel
import com.theseuntaylor.picsomeapp.lib_home.data.fakes.FakePhotosRepository
import com.theseuntaylor.picsomeapp.lib_home.data.fakes.FakePhotosRepository.Companion.photos
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/** Home's first load and paging. Each test guards one failure mode. */
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val firstPage = photos(1..3)
    private val secondPage = photos(4..6)
    private val repository = FakePhotosRepository(photos = firstPage)

    // region first load

    @Test
    fun `first load failing shows its error rather than a blank screen`() = runTest {
        repository.loadError = Throwable("Something went wrong!")

        assertEquals(HomeUiState.Error("Something went wrong!"), HomeViewModel(repository).uiState.value)
    }

    @Test
    fun `first load failing without a message falls back to a generic error`() = runTest {
        repository.loadError = Throwable()

        assertEquals(
            HomeUiState.Error("There was an error loading images"),
            HomeViewModel(repository).uiState.value
        )
    }

    @Test
    fun `first load shows loading, then the photos in the order they were cached`() = runTest {
        val gate = CompletableDeferred<Unit>().also { repository.photosGate = it }
        val viewModel = HomeViewModel(repository)
        assertEquals(HomeUiState.Loading, viewModel.uiState.value)

        gate.complete(Unit)

        assertEquals(listOf("1", "2", "3"), viewModel.success().data.map { it.id })
    }

    // endregion

    // region loading more

    @Test
    fun `loading more before the first page has loaded does nothing`() = runTest {
        repository.photosGate = CompletableDeferred()
        val viewModel = HomeViewModel(repository)

        viewModel.loadMorePhotos()

        assertEquals(0, repository.loadNextPageCalls)
        assertEquals(HomeUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `loading more while the first load is showing an error does nothing`() = runTest {
        repository.loadError = Throwable("offline")
        val viewModel = HomeViewModel(repository)

        viewModel.loadMorePhotos()

        assertEquals(0, repository.loadNextPageCalls)
    }

    @Test
    fun `loading more again while a page is still loading does not request it twice`() = runTest {
        val gate = CompletableDeferred<Unit>()
        repository.nextPage = { gate.await(); true }
        val viewModel = HomeViewModel(repository)

        viewModel.loadMorePhotos()
        viewModel.loadMorePhotos()
        viewModel.loadMorePhotos()

        assertEquals(1, repository.loadNextPageCalls)
    }

    @Test
    fun `loading more after the last page does not keep requesting pages`() = runTest {
        repository.nextPage = { false }
        val viewModel = HomeViewModel(repository)
        viewModel.loadMorePhotos()
        assertTrue(viewModel.success().endReached)

        viewModel.loadMorePhotos()

        assertEquals(1, repository.loadNextPageCalls)
    }

    @Test
    fun `a loaded page shows the new photos after the existing ones and stops the spinner`() = runTest {
        val gate = CompletableDeferred<Unit>()
        repository.nextPage = { gate.await(); repository.photos += secondPage; true }
        val viewModel = HomeViewModel(repository)

        viewModel.loadMorePhotos()
        assertTrue(viewModel.success().isLoadingMore)
        gate.complete(Unit)

        with(viewModel.success()) {
            assertEquals(listOf("1", "2", "3", "4", "5", "6"), data.map { it.id })
            assertFalse(isLoadingMore)
            assertFalse(endReached)
            assertNull(loadMoreError)
        }
    }

    @Test
    fun `an empty last page marks the end of the list`() = runTest {
        repository.nextPage = { false }
        val viewModel = HomeViewModel(repository)

        viewModel.loadMorePhotos()

        with(viewModel.success()) {
            assertTrue(endReached)
            assertFalse(isLoadingMore)
            assertEquals(3, data.size)
        }
    }

    @Test
    fun `a page failing to load keeps the photos on screen and explains why`() = runTest {
        repository.nextPage = { throw Throwable("There seems to be no network connection") }
        val viewModel = HomeViewModel(repository)

        viewModel.loadMorePhotos()

        with(viewModel.success()) {
            assertEquals(listOf("1", "2", "3"), data.map { it.id })
            assertFalse(isLoadingMore)
            assertEquals("There seems to be no network connection", loadMoreError)
        }
    }

    @Test
    fun `a page failing without a message falls back to a generic error`() = runTest {
        repository.nextPage = { throw Throwable() }
        val viewModel = HomeViewModel(repository)

        viewModel.loadMorePhotos()

        assertEquals("There was an error loading more images", viewModel.success().loadMoreError)
    }

    @Test
    fun `retrying a failed page clears the old error while it loads`() = runTest {
        repository.nextPage = { throw Throwable("offline") }
        val viewModel = HomeViewModel(repository)
        viewModel.loadMorePhotos()

        val gate = CompletableDeferred<Unit>()
        repository.nextPage = { gate.await(); repository.photos += secondPage; true }
        viewModel.loadMorePhotos()

        with(viewModel.success()) {
            assertTrue(isLoadingMore)
            assertNull(loadMoreError)
        }
        gate.complete(Unit)
        assertEquals(6, viewModel.success().data.size)
    }

    @Test
    fun `a page load cancelled because the screen closed is not shown as an error`() = runTest {
        repository.nextPage = { throw CancellationException("screen closed") }
        val viewModel = HomeViewModel(repository)

        viewModel.loadMorePhotos()

        assertNull(viewModel.success().loadMoreError)
    }

    // endregion

    // region favourites

    @Test
    fun `favouriting while a page loads keeps the spinner, and the page keeps the favourite`() = runTest {
        val gate = CompletableDeferred<Unit>()
        repository.nextPage = { gate.await(); repository.photos += secondPage; true }
        val viewModel = HomeViewModel(repository)
        viewModel.loadMorePhotos()

        viewModel.toggleFavourite(id = "2", isFavourite = true)
        assertTrue("Favouriting stopped the spinner", viewModel.success().isLoadingMore)

        gate.complete(Unit)
        with(viewModel.success()) {
            assertEquals(6, data.size)
            assertTrue("The new page undid the favourite", data.single { it.id == "2" }.isFavourite)
        }
    }

    @Test
    fun `favouriting keeps the end-of-list and retry states`() = runTest {
        repository.nextPage = { throw Throwable("offline") }
        val viewModel = HomeViewModel(repository)
        viewModel.loadMorePhotos()

        viewModel.toggleFavourite(id = "1", isFavourite = true)

        with(viewModel.success()) {
            assertEquals("offline", loadMoreError)
            assertTrue(data.single { it.id == "1" }.isFavourite)
        }
    }

    @Test
    fun `a favourite that fails to save leaves the photos as they were instead of crashing`() = runTest {
        repository.toggleError = IllegalStateException("database is locked")
        val viewModel = HomeViewModel(repository)

        viewModel.toggleFavourite(id = "1", isFavourite = true)

        assertFalse(viewModel.success().data.single { it.id == "1" }.isFavourite)
    }

    // endregion

    private fun HomeViewModel.success() = uiState.value as HomeUiState.Success
}
