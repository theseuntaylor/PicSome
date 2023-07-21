package com.application.storelabs.lib_home.di

import com.application.storelabs.lib_home.data.repository.PhotosRepository
import com.application.storelabs.lib_home.data.repository.PhotosRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    @Singleton
    abstract fun bindPhotosRepository(
        photosRepositoryImpl: PhotosRepositoryImpl
    ): PhotosRepository

}