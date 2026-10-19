package com.theseuntaylor.picsomeapp.feature.detail

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.theseuntaylor.picsomeapp.R
import com.theseuntaylor.picsomeapp.core.MainDispatcherRule
import com.theseuntaylor.picsomeapp.core.downloader.Downloader
import com.theseuntaylor.picsomeapp.feature.detail.model.PhotoDetailsUiState
import com.theseuntaylor.picsomeapp.feature.detail.ui.PhotoDetailsViewModel
import com.theseuntaylor.picsomeapp.feature.home.model.asUiModel
import com.theseuntaylor.picsomeapp.lib_home.data.fakes.FakePhotosRepository
import com.theseuntaylor.picsomeapp.lib_home.data.fakes.FakePhotosRepository.Companion.photo
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock

/** Photo details and downloading. Each test guards one failure mode. */
class PhotoDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val photo = photo(id = "42", author = "Paul Evans")
    private val repository = FakePhotosRepository(photos = listOf(photo))
    private val downloader = RecordingDownloader()
    private val context = mock<Context> {
        on { getString(eq(R.string.download_title), any()) } doReturn "Photo by Paul Evans"
    }

    // region loading the photo

    @Test
    fun `a found photo is shown`() = runTest {
        assertEquals(PhotoDetailsUiState.Success(photo.asUiModel()), viewModel("42").uiState.value)
    }

    @Test
    fun `a photo that can't be loaded shows an error rather than a blank screen`() = runTest {
        repository.loadError = IllegalStateException("The query result was empty")

        assertEquals(PhotoDetailsUiState.Error("The query result was empty"), viewModel("42").uiState.value)
    }

    @Test
    fun `a photo that can't be loaded without a message falls back to a generic error`() = runTest {
        repository.loadError = Throwable()

        assertEquals(PhotoDetailsUiState.Error("There was an error loading image"), viewModel("42").uiState.value)
    }

    @Test
    fun `opening details without a photo id does not crash`() = runTest {
        val viewModel = viewModel(photoId = null)

        assertEquals(PhotoDetailsUiState.Initial, viewModel.uiState.value)
    }

    // endregion

    // region downloading

    @Test
    fun `downloading before the photo has loaded does nothing`() = runTest {
        repository.loadError = Throwable("not found")
        val viewModel = viewModel("42")

        viewModel.downloadPhoto()

        assertTrue(downloader.requests.isEmpty())
        assertNull(viewModel.downloadMessage.value)
    }

    @Test
    fun `downloading saves the full-size original, named after the photo, and says it started`() = runTest {
        val viewModel = viewModel("42")

        viewModel.downloadPhoto()

        assertEquals(
            listOf(Request("https://picsum.photos/id/42/4000/3000", "picsome_42.jpg", "Photo by Paul Evans")),
            downloader.requests
        )
        assertEquals(R.string.download_started, viewModel.downloadMessage.value)
    }

    @Test
    fun `a download the system refuses says it failed instead of crashing`() = runTest {
        downloader.failure = IllegalStateException("Not one of standard directories")
        val viewModel = viewModel("42")

        viewModel.downloadPhoto()

        assertEquals(R.string.download_failed, viewModel.downloadMessage.value)
    }

    @Test
    fun `denying storage permission explains it is needed`() = runTest {
        val viewModel = viewModel("42")

        viewModel.onStoragePermissionDenied()

        assertEquals(R.string.download_needs_storage_permission, viewModel.downloadMessage.value)
    }

    @Test
    fun `once a download message is shown, the next download shows one again`() = runTest {
        val viewModel = viewModel("42")
        viewModel.downloadPhoto()

        viewModel.onDownloadMessageShown()
        assertNull(viewModel.downloadMessage.value)

        viewModel.downloadPhoto()
        assertEquals(R.string.download_started, viewModel.downloadMessage.value)
    }

    // endregion

    private fun viewModel(photoId: String?) = PhotoDetailsViewModel(
        savedStateHandle = SavedStateHandle(if (photoId != null) mapOf("photoId" to photoId) else emptyMap()),
        repository = repository,
        downloader = downloader,
        context = context,
    )

    private data class Request(val url: String, val fileName: String, val title: String)

    private class RecordingDownloader : Downloader {
        val requests = mutableListOf<Request>()
        var failure: Exception? = null

        override fun downloadFile(url: String, fileName: String, title: String): Long {
            failure?.let { throw it }
            requests += Request(url, fileName, title)
            return requests.size.toLong()
        }
    }
}
