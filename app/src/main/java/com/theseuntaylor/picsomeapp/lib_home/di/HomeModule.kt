package com.theseuntaylor.picsomeapp.lib_home.di

import com.theseuntaylor.picsomeapp.lib_home.data.repository.PhotosRepository
import com.theseuntaylor.picsomeapp.lib_home.data.repository.PhotosRepositoryImpl
import com.theseuntaylor.picsomeapp.lib_home.domain.model.PhotoImageUrls
import com.theseuntaylor.picsomeapp.lib_home.domain.usecase.FavouritesFunctionalUseCase
import com.theseuntaylor.picsomeapp.lib_home.remote.PicsumPhotoImageUrls
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    companion object {
        @Provides
        fun provideFavouriteFunctionalUseCase(repository: PhotosRepository) =
            FavouritesFunctionalUseCase(
                repository::getFavouritePhotos
            )
    }

    @Binds
    @Singleton
    abstract fun bindPhotosRepository(
        photosRepositoryImpl: PhotosRepositoryImpl,
    ): PhotosRepository

    @Binds
    abstract fun bindPhotoImageUrls(picsumPhotoImageUrls: PicsumPhotoImageUrls): PhotoImageUrls

}