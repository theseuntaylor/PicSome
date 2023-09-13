package com.theseuntaylor.picsomeapp.feature.favourites

import com.theseuntaylor.picsomeapp.feature.favourites.ui.FavouriteViewModel
import com.theseuntaylor.picsomeapp.lib_home.data.fakes.DummyData
import com.theseuntaylor.picsomeapp.lib_home.data.repository.PhotosRepository
import com.theseuntaylor.picsomeapp.lib_home.domain.usecase.FavouriteUseCase
import com.theseuntaylor.picsomeapp.lib_home.domain.usecase.FavouritesFunctionalUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.random.Random


class FavouriteViewModelTest {
    // region Tests
    @Test
    fun `when the favourite screen is created the ui state is initialised`() = runTest {
        // given
        val expectedUiState = FavouritesUiState.Initial
        val favouriteViewModel = createFavouriteViewModel()
        // when
        val actualUiState = favouriteViewModel.uiState.value
        // then
        Assert.assertEquals(expectedUiState, actualUiState)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when we fetch for favourite photos, then we fetch for favourite photos from the repository`() =
        runTest {
            // given
            val photosRepository = mock<PhotosRepository>().apply {
                whenever(getFavouritePhotos()).thenReturn(flowOf(DummyData.favouritePhotos))
            }

            val usecase = FavouriteUseCase(photosRepository = photosRepository)

            val favouriteViewModel = createFavouriteViewModel(
                photosRepository = photosRepository,
                favouriteUseCase = usecase
            )

            try {
                // when
                val testDispatcher = UnconfinedTestDispatcher(testScheduler)
                Dispatchers.setMain(testDispatcher)
                favouriteViewModel.getFavouritePhotos()
                // then
                verify(photosRepository).getFavouritePhotos()
            } finally {
                Dispatchers.resetMain()
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when we fetch for favourite photos, then we get the expected photos if it is successful`() =
        runTest {
            // given
            val expectedUiState =
                FavouritesUiState.Success(favouritePhotos = DummyData.favouritePhotosUi)
            val photosRepository = mock<PhotosRepository>().apply {
                whenever(getFavouritePhotos()).thenReturn(flowOf(DummyData.favouritePhotos))
            }

            val usecase = FavouriteUseCase(photosRepository = photosRepository)

            // does not need to be mocked
//            val functionalUseCase = FavouritesFunctionalUseCase(
//                photosRepository::getFavouritePhotos
//            )
//            functionalUseCase.invoke()

            val favouriteViewModel = createFavouriteViewModel(
                photosRepository = photosRepository,
                favouriteUseCase = usecase
            )
            try {
                // when
                val testDispatcher = UnconfinedTestDispatcher(testScheduler)
                Dispatchers.setMain(testDispatcher)
                favouriteViewModel.getFavouritePhotos()
                val actualUiState = favouriteViewModel.uiState.value
                // then
                Assert.assertEquals(expectedUiState, actualUiState)
            } finally {
                Dispatchers.resetMain()
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when we toggle a favourite photo, then we set if it is liked from the repository`() =
        runTest {
            // given
            val isFavourite = Random.nextBoolean()
            val photosRepository = mock<PhotosRepository>().apply {
                whenever(toggleFavourite(id = any(), isFavourite = any()))
                    .thenReturn(Unit)
                whenever(getFavouritePhotos()).thenReturn(flowOf(DummyData.favouritePhotos))

            }
            val favouriteViewModel = createFavouriteViewModel(photosRepository = photosRepository)
            try {
                // when
                val testDispatcher = UnconfinedTestDispatcher(testScheduler)
                Dispatchers.setMain(testDispatcher)
                favouriteViewModel.toggleFavourite(id = "103", isFavourite = isFavourite)
                // then
                verify(photosRepository).toggleFavourite(id = "103", isFavourite = isFavourite)
            } finally {
                Dispatchers.resetMain()
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when we toggle a favourite photo, then we fetch new favourite photos from the repository`() =
        runTest {
            // given
            val photosRepository = mock<PhotosRepository>().apply {
                whenever(toggleFavourite(id = any(), isFavourite = any()))
                    .thenReturn(Unit)
                whenever(getFavouritePhotos()).thenReturn(flowOf(DummyData.favouritePhotos))

            }
            val favouriteViewModel = createFavouriteViewModel(photosRepository = photosRepository)
            try {
                // when
                val testDispatcher = UnconfinedTestDispatcher(testScheduler)
                Dispatchers.setMain(testDispatcher)
                favouriteViewModel.toggleFavourite(id = "103", isFavourite = Random.nextBoolean())
                // then
                verify(photosRepository).getFavouritePhotos()
            } finally {
                Dispatchers.resetMain()
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when we toggle a favourite photo, then new favourite photos are sent`() = runTest {
        // given
        val expectedUiState =
            FavouritesUiState.Success(favouritePhotos = listOf(DummyData.favouritePhotosUi.last()))
        val photosRepository = mock<PhotosRepository>().apply {
            whenever(toggleFavourite(id = "102", isFavourite = false))
                .thenReturn(Unit)
            whenever(getFavouritePhotos()).thenReturn(flowOf(listOf(DummyData.favouritePhotos.last())))

        }
        val usecase = FavouriteUseCase(photosRepository = photosRepository)

        val favouriteViewModel = createFavouriteViewModel(
            photosRepository = photosRepository,
            favouriteUseCase = usecase
        )
        try {
            // when
            val testDispatcher = UnconfinedTestDispatcher(testScheduler)
            Dispatchers.setMain(testDispatcher)
            favouriteViewModel.toggleFavourite(id = "102", isFavourite = false)
            // then
            val actualUiState = favouriteViewModel.uiState.value
            // then
            Assert.assertEquals(expectedUiState, actualUiState)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when we unlike a favourite photo, then new favourite photos are sent`() = runTest {
        // given
        val photoId = "102"
        val expectedUiState =
            FavouritesUiState.Success(favouritePhotos = listOf(DummyData.favouritePhotosUi.last()))
        val photosRepository = mock<PhotosRepository>().apply {
            whenever(toggleFavourite(id = photoId, isFavourite = false))
                .thenReturn(Unit)
            whenever(getFavouritePhotos()).thenReturn(flowOf(
                DummyData.favouritePhotos.filter { photo -> photo.id != photoId }
            ))
        }
        val usecase = FavouriteUseCase(photosRepository = photosRepository)


        val favouriteViewModel = createFavouriteViewModel(
            photosRepository = photosRepository,
            favouriteUseCase = usecase
        )
        try {
            // when
            val testDispatcher = UnconfinedTestDispatcher(testScheduler)
            Dispatchers.setMain(testDispatcher)
            favouriteViewModel.toggleFavourite(id = photoId, isFavourite = false)
            // then
            val actualUiState = favouriteViewModel.uiState.value
            // then
            Assert.assertEquals(expectedUiState, actualUiState)
        } finally {
            Dispatchers.resetMain()
        }
    }

    // endregion

    // region private helpers
    private val ERROR_MSG = "Something went wrong!"
    
    private fun createFavouriteViewModel(
        photosRepository: PhotosRepository = mock(),
        favouriteUseCase: FavouriteUseCase = mock(),
        favouritesFunctionalUseCase: FavouritesFunctionalUseCase = mock(),
    ) = FavouriteViewModel(
        repository = photosRepository,
        favouriteUseCase = favouriteUseCase,
        // functionalUseCase = favouritesFunctionalUseCase
    )
    // endregion
}