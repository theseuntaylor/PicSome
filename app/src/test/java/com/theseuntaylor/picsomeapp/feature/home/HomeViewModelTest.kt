package com.theseuntaylor.picsomeapp.feature.home

import com.theseuntaylor.picsomeapp.core.MainDispatcherRule
import com.theseuntaylor.picsomeapp.feature.home.model.HomeUiState
import com.theseuntaylor.picsomeapp.feature.home.ui.HomeViewModel
import com.theseuntaylor.picsomeapp.lib_home.data.repository.PhotosRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever


class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when we fetch for photos, then we get the error message if it is not successful`() =
        runTest {
            // given
            val expectedUiState =
                HomeUiState.Error(
                    errorMessage = ERROR_MSG
                )
            val photosRepository = mock<PhotosRepository>().apply {
                whenever(getPhotos()).thenReturn(
                    flow {
                        throw Throwable(ERROR_MSG)
                    }
                )
            }

            val homeViewModel = createHomeViewModel(
                photosRepository = photosRepository,
            )
                // when

                homeViewModel.getPhotos()

                val actualUiState = homeViewModel.uiState.value

                // then

                Assert.assertEquals(expectedUiState, actualUiState)

        }

    // region private helpers
    private val ERROR_MSG = "Something went wrong!"
    private fun createHomeViewModel(
        photosRepository: PhotosRepository = mock(),
    ) = HomeViewModel(
        repository = photosRepository,
        // functionalUseCase = favouritesFunctionalUseCase
    )
    // endregion
}